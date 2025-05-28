#include <iostream>
#include <thread>
#include <chrono>
#include <vector>
#include <memory>
#include <semaphore>
#include <mutex>

using namespace std;

mutex cout_mutex;

class TallerMecanico 
{
private:
    const int N;

   counting_semaphore<6> espacioPlaya = counting_semaphore<6>(6);
   counting_semaphore<6> autosParaInspeccion = counting_semaphore<6>(0);
   counting_semaphore<3> espacioFosa = counting_semaphore<3>(3);
   counting_semaphore<6> autosParaReparar = counting_semaphore<6>(0);
   counting_semaphore<2> espacioServicio = counting_semaphore<2>(2);
   counting_semaphore<1> espacioLavado = counting_semaphore<1>(1);
   counting_semaphore<1> autoEnReparacion = counting_semaphore<1>(1);
   counting_semaphore<1> autoEnInspeccion = counting_semaphore<1>(1);

public:
    TallerMecanico(int n) : N(n) 
    {

    }

    void richard(int id) 
    {
        espacioPlaya.acquire();
        {
            lock_guard<mutex> lock(cout_mutex);
            cout << "[Auto " << id << "] Richard lo ingresa a la playa." << endl;
        }
        this_thread::sleep_for(chrono::milliseconds(100));
        autosParaInspeccion.release();
    }

    void aaron(int id) 
    {
        autosParaInspeccion.acquire();
        autoEnInspeccion.acquire();
        {
            lock_guard<mutex> lock(cout_mutex);
            cout << "[Auto " << id << "] Aaron lo inspecciona." << endl;
            this_thread::sleep_for(chrono::milliseconds(100));
        }
        autoEnInspeccion.release();
        espacioFosa.acquire();
        autosParaReparar.release();
    }

    void charles(int id) 
    {
        autosParaReparar.acquire();
        autoEnReparacion.acquire();
        {
            lock_guard<mutex> lock(cout_mutex);
            cout << "[Auto " << id << "] ayudante lo traslada a zona de fosa y Charles lo repara." << endl;
        }
        this_thread::sleep_for(chrono::milliseconds(100));
        autoEnReparacion.release();
        espacioServicio.acquire();
        espacioFosa.release();
    }

    void ayudante(int id) 
    {
        {
            lock_guard<mutex> lock(cout_mutex);
            cout << "[Auto " << id << "] Ayudante lo traslada a zona de servicio y cambia el aceite." << endl;
        }
        this_thread::sleep_for(chrono::milliseconds(100));
        espacioServicio.release();

        espacioLavado.acquire();
        {
            lock_guard<mutex> lock(cout_mutex);
            cout << "[Auto " << id << "] Se lava y se lo transporta a playa." << endl;
        }
        this_thread::sleep_for(chrono::milliseconds(100));
        espacioLavado.release();

        espacioPlaya.release();
    }

    void ingresarAuto(int id) 
    {
        richard(id);
        aaron(id);
        charles(id);
        ayudante(id);
    }

    void iniciar() 
    {
        vector<thread> threads;
        for (int i = 0; i < N; ++i) 
        {
            threads.emplace_back(&TallerMecanico::ingresarAuto, this, i + 1);
        }
        for (auto& t : threads) 
        {
            t.join();
        }
        {
            lock_guard<mutex> lock(cout_mutex);
            cout << "Todos los autos fueron atendidos y retirados." << endl;
        }
    }
};


int main(int argc, char* argv[]) 
{
    if (argc != 2) 
    {
        cerr << "Uso: " << argv[0] << " <N_AUTOS>\n";
        return 1;
    }

    int N = stoi(argv[1]);
    TallerMecanico taller(N);
    taller.iniciar();

    return 0;
}