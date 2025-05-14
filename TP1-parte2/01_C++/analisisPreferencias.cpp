#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>

#define MAX_USERS 100
#define MAX_LINES 1000
#define MAX_FIELD 100
#define ERR_FILE  1
#define OK_FILE   0
#define MAX_LINE  512
#define MAX_GENRE 100
#define NOT_EXIST_IDX -1
#define EXIST_GENRE 2
#define NOT_EXIST_GENRE 3
#define MAX_VIEW 1000
#define MAX_USER_VIEW 200

typedef struct 
{
    int user_id;
    char user_name[MAX_FIELD];
    char title[MAX_FIELD];
    char type[MAX_FIELD];
    char genre[MAX_FIELD];
} View;

typedef struct 
{
    int user_id;
    char user_name[MAX_FIELD];
    View* views[MAX_VIEW];
    int view_count;
} UserData;

typedef struct 
{
    char chosen_genre[MAX_FIELD];
    char chosen_type[MAX_FIELD];
    int total;
    int different_genres;
    int idx_preference;
    UserData* user;
} Preference;


int read_views(const char* filename, View views[]);
int logUserViews(View all_views[], int total_views, UserData users[]);
void countType(View* v, int type_count[]);
void countGenre(View* v, int* unique_genres, int genre_counts[], char genres[][MAX_FIELD]);
void type_most_frequent(char chosen_type[], int type_count[]);
int genre_most_frequent(int unique_genres, int genre_count[]);
void saveResult(Preference pref, char genres[][MAX_FIELD], char chosen_type[], int unique_genres, int max_index);
int write_json(const char* filename, int total_users, Preference pre[]);
int find_user_index(int user_id, const UserData users[], int user_count);



int read_views(const char* filename, View views[]) 
{
    FILE* f = fopen(filename, "r");
    if (f == NULL) 
    {
        perror("Error al abrir el archivo");
        exit(EXIT_FAILURE);
    }

    int total_views = 0;
    char line[MAX_LINE];

    fgets(line, sizeof(line), f);

    while (fgets(line, sizeof(line), f)) 
    {
        if (total_views >= MAX_VIEW) break;

        sscanf(line, "%d,%99[^,],%99[^,],%99[^,],%99[^\n]",
                      &views[total_views].user_id,
                      views[total_views].user_name,
                      views[total_views].title,
                      views[total_views].type,
                      views[total_views].genre
              );

        total_views++;
    }

    fclose(f);

    return total_views;
}




int find_user_index(int user_id, const UserData users[], int user_count) 
{
    for (int i = 0; i < user_count; i++) 
    {
        if (users[i].user_id == user_id)
            return i;
    }
    return NOT_EXIST_IDX;
}




int logUserViews(View all_views[], int total_views, UserData users[])
{
    int user_count = 0;
    for(int i = 0; i < total_views; i++)
    {
      int idx = find_user_index(all_views[i].user_id, users, user_count);
        if (idx == NOT_EXIST_IDX) 
        {
            idx = user_count;
            users[user_count].user_id = all_views[i].user_id;
            strcpy(users[user_count].user_name, all_views[i].user_name);
            users[user_count].view_count = 0;
            user_count++;
        }

        if (users[idx].view_count < MAX_USER_VIEW) 
        {
          users[idx].views[users[idx].view_count++] = &all_views[i];
        }

    }

    return user_count;
}




void countType(View* v, int type_count[])
{
  if (strcmp(v->type, "Serie") == 0)
    type_count[0]++;
  else if (strcmp(v->type, "Película") == 0)
    type_count[1]++;
}





void countGenre(View* v, int* unique_genres, int genre_counts[], char genres[][MAX_FIELD])
{
  int found = NOT_EXIST_GENRE;
  for (int i = 0; i < *unique_genres; i++) 
  {
      if (strcmp(genres[i], v->genre) == 0) 
      {
          genre_counts[i]++;
          found = EXIST_GENRE;
          break;
      }
  }
  if (found == NOT_EXIST_GENRE) 
  {
      strcpy(genres[*unique_genres], v->genre);
      genre_counts[*unique_genres] = 1;
      (*unique_genres)++;
  }
}





void type_most_frequent(char chosen_type[], int type_count[])
{
  strcpy(chosen_type, (type_count[1] >= type_count[0]) ? "Película" : "Serie");
}





int genre_most_frequent(int unique_genres, int genre_counts[])
{
  int max_index = 0;
  for (int i = 1; i < unique_genres; i++) 
  {
      if (genre_counts[i] > genre_counts[max_index]) 
      {
          max_index = i;
      }
  }

  return max_index;
}





void saveResult(Preference* pref, char genres[][MAX_FIELD], char chosen_type[], int unique_genres, int max_index)
{
    strcpy(pref->chosen_genre, genres[max_index]);
    strcpy(pref->chosen_type, chosen_type);
    pref->different_genres = unique_genres;
}





void* analyze_user(void* arg) 
{
    Preference* preferenceUser = (Preference*)arg;

    int genre_counts[MAX_GENRE] = 
    {0
    };
    int type_count[2] = 
    {0
    };
    char genres[MAX_GENRE][MAX_FIELD];
    int unique_genres = 0;

    for (int i = 0; i < preferenceUser->user->view_count; i++) 
    {
        View* v = preferenceUser->user->views[i];

        countType(v, type_count);
        countGenre(v, &unique_genres, genre_counts, genres);
    }

    char chosen_type[MAX_FIELD];
    type_most_frequent(chosen_type, type_count);
    int max_index = genre_most_frequent(unique_genres, genre_counts);

    saveResult(preferenceUser, genres, chosen_type, unique_genres, max_index);

    return NULL;
}




int write_json(const char* filename, int total_users, Preference pre[]) 
{   
    FILE* f = fopen(filename, "w");
    if (!f) return ERR_FILE;

    fprintf(f, "[\n");
    for (int i = 0; i < total_users; i++)
    {
        Preference p = pre[i];
        fprintf(    f,
                    "  {\n"
                    "    \"user_id\": %d,\n"
                    "    \"user_name\": \"%s\",\n"
                    "    \"chosen_genre\": \"%s\",\n"
                    "    \"chosen_type\": \"%s\",\n"
                    "    \"total\": %d,\n"
                    "    \"different_genres\": %d\n"
                    "  }%s\n",
                    p.user->user_id, p.user->user_name, p.chosen_genre, p.chosen_type,
                    p.user->view_count, p.different_genres,
                    (i == total_users - 1) ? "" : ","
               );
    }
    fprintf(f, "]\n");
    fclose(f);

    return OK_FILE;
}





int main(int argc, char* argv[])
{
    if (argc != 2) 
    {
      fprintf(stderr, "Uso: %s <archivo_visualizaciones.csv>\n", argv[0]);
      return ERR_FILE;
    }

    const char* input_filename = argv[1];

    View all_views[MAX_VIEW];
    int total_views = read_views(input_filename, all_views);

    UserData users[MAX_USERS];
    int user_count = logUserViews(all_views, total_views, users);

    
    Preference preferenceUser[MAX_USERS];
    pthread_t threads[MAX_USERS];
    for (int i = 0; i < user_count; i++) 
    {
        preferenceUser[i].user = &users[i];
        if (pthread_create(&threads[i], NULL, analyze_user, &preferenceUser[i]) != 0) 
        {
          perror("Error creando hilo");
          exit(EXIT_FAILURE);
        }
    }

    for (int i = 0; i < user_count; i++) 
    {
        pthread_join(threads[i], NULL);
    }

    if (write_json("preferencias.json", user_count, preferenceUser) == ERR_FILE) 
    {
        perror("Error al escribir JSON");
        return ERR_FILE;
    }

    printf("preferencias.json generado con éxito.\n");
    
    return OK_FILE;
}