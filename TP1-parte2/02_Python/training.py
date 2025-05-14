import threading
from random import randint,uniform
from time import sleep,perf_counter

def training(clones):
  result = [0]*clones

  def train(clone_number):
    nonlocal result
    chakra = 5+randint(0,5)
    level = randint(0,1)
    wait_time = uniform(0.1,0.2)
    for _ in range(chakra):
      result[clone_number]+=level
      sleep(wait_time)

  threads = list()

  for clone_number in range(clones):
    threads.append(threading.Thread(target=train,args=(clone_number,)))
    threads[clone_number].start()

  for thread in threads:
    thread.join()

  return sum(result)
  
def main():
  clone_range = range(5,30,5)
  print("Comenzando entrenamiento:")
  for clone_number in clone_range:
    start = perf_counter()
    training(clone_number)
    print("Cantidad de clones:",clone_number,"\nTiempo de ejecucion:",perf_counter()-start,"Segundos.\n",sep=" ",end="\n") 

if __name__ == "__main__":
    main()