
import sys
import os
import time
import random

PLAYER = 5
THROWS = 10

def player(id):
  sys.stdout.write(f"Jugador {id} entra al juego.\n")
  points = 0
  for i in range(THROWS):
    dice = random.randint(1, 6)
    points += dice
    sys.stdout.write(f"Jugador {id} - Lanzamiento {i+1}: {dice}\n")
    time.sleep(random.uniform(0.1, 0.3))
  sys.stdout.write(f"Jugador {id} finaliza con {points} puntos.\n")

def main():
  for i in range(PLAYER):
    pid = os.fork()

    if pid < 0:
      sys.exit("Error al crear el nuevo proceso")

    if pid == 0:
      player(i + 1)
      sys.exit(0)

  for _ in range(PLAYER):
    os.wait()

  print("Todos los jugadores han terminado.")

if __name__ == "__main__":
    main()