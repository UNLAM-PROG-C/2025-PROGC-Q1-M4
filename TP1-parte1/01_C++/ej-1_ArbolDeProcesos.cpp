
#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>

#include <iostream>

using namespace std;

void crear_proceso(char letra) 
{
  pid_t pid = getpid();
  pid_t ppid = getppid();
  cout << "proceso" << letra << " con PID: " << pid << " PPID: " << ppid
       << endl;

  if (letra == 'A') 
  {
    pid_t b;
    b = fork();
    if (b == 0) 
    {
      crear_proceso('B');
      exit(0);
    }
    sleep(20);
    waitpid(b, NULL, 0);
  } else if (letra == 'B') 
  {
    pid_t c, d;
    c = fork();
    if (c == 0) 
    {
      crear_proceso('C');
      exit(0);
    }
    d = fork();
    if (d == 0) 
    {
      crear_proceso('D');
      exit(0);
    }
    sleep(20);
    waitpid(c, NULL, 0);
    waitpid(d, NULL, 0);
  } else if (letra == 'C') 
  {
    pid_t e;
    e = fork();
    if (e == 0) 
    {
      crear_proceso('E');
      exit(0);
    }
    sleep(20);
    waitpid(e, NULL, 0);
  } else if (letra == 'E') 
  {
    pid_t h, i;
    h = fork();
    if (h == 0) 
    {
      crear_proceso('H');
      sleep(20);
      exit(0);
    }
    i = fork();
    if (i == 0) 
    {
      crear_proceso('I');
      sleep(20);
      exit(0);
    }
    sleep(20);
    waitpid(h, NULL, 0);
    waitpid(i, NULL, 0);
  } else if (letra == 'D') 
  {
    pid_t f, g;
    f = fork();
    if (f == 0) 
    {
      crear_proceso('F');
      exit(0);
    }
    g = fork();
    if (g == 0) 
    {
      crear_proceso('G');
      exit(0);
    }
    sleep(20);
    waitpid(f, NULL, 0);
    waitpid(g, NULL, 0);
  }

  sleep(20);
}

int main() 
{

  crear_proceso('A');
  sleep(20);
  

  return 0;
}