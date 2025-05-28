import threading
import queue
import time
import random
import signal
import os

endAll= False

mainTable = []
max_capacity_main = 20
ovenBasket = []
max_capacity_oven = 50
standSale = queue.Queue(maxsize=30)


lock_main = threading.Lock()
lock_baking_spot = threading.Lock()
lock_packer = threading.Lock()
lock_sale = threading.Lock()
condition_main = threading.Condition(lock_main)
condition_baking = threading.Condition(lock_baking_spot)
condition_packer = threading.Condition(lock_packer)
condition_sale = threading.Condition(lock_sale)

counter_master = 0
counter_junior = 0
sale_counter = 1 

def signal_manager(signum, frame):
    global endAll
    print("\nParent process received termination signal.")
    endAll = True

signal.signal(signal.SIGUSR1, signal_manager)

def bakerMaster():
    global counter_master
    while True:
        time.sleep(4)
        with condition_main:
            while len(mainTable) >= max_capacity_main:
                print("🧑‍🍳 bakerMaster entra en espera...")
                condition_main.wait()
                print("🧑‍🍳 bakerMaster despierta de la espera...")
            mainTable.append(f"masaBM{counter_master}")
            counter_master += 1
            mainTable.append(f"masaBM{counter_master}")
            counter_master += 1
            print(f"🧑‍🍳 bakerMaster agregó: {mainTable[-2:]}")
            
            if len(mainTable) == max_capacity_main:
                print("🧑‍🍳 baker notifico mainTable  llena")
                condition_main.notify()
                with condition_baking:
                    condition_baking.notify()

def bakerJunior():
    global counter_junior
    while True:
        time.sleep(5)
        with condition_main:
            while len(mainTable) >= max_capacity_main:
                print("🧑‍🍳 bakerJunior entra en espera...")
                condition_main.wait()
                print("🧑‍🍳 bakerJunior despierta de la espera...")
            mainTable.append(f"masaBJ{counter_junior}")
            counter_junior += 1
            print(f"🧑‍🍳 bakerJunior agregó: {mainTable[-1]}")
            
            if len(mainTable) == max_capacity_main:
                print("🧑‍🍳 bakerJunior notifico mainTable llena")
                condition_main.notify()
                with condition_baking:
                    condition_baking.notify()

def bakedStep():
    global ovenBasket
    while True:
        with condition_main:
            while len(mainTable) < max_capacity_main:
                print("🔥 bakedStep entra en espera ...")
                condition_main.wait()
                print("🔥 bakedStep despierta de la espera ...")
            with condition_baking:
                while len(mainTable) < max_capacity_main: 
                    condition_baking.wait()

                while mainTable:
                    with condition_packer:
                        while len(ovenBasket) >= max_capacity_oven:
                            condition_packer.wait()

                        batch = [mainTable.pop(0) + 'H' for _ in range(min(5, len(mainTable)))]
                        time.sleep(6)
                        ovenBasket.extend(batch)
                        print(f"🔥 bakedStep movió al horno: {batch}")

                        if len(ovenBasket) == max_capacity_oven:
                            condition_packer.notify_all() 
                            condition_packer.wait()

def packer1():
    while True:
        time.sleep(2)
        with condition_packer:
            while len(ovenBasket) < 3: 
                condition_packer.wait()

            if len(ovenBasket) >= 3 and not standSale.full():
                packed_item = f"[{ovenBasket.pop(0)}-{ovenBasket.pop(0)}-{ovenBasket.pop(0)}]"
                standSale.put(packed_item)
                print(f"📦 packer1 empacó: {packed_item}")
                time.sleep(3)
            
            if len(ovenBasket) < 3 and not standSale.full():
               condition_packer.notify() 

            with condition_sale:
                if standSale.full():
                    condition_sale.notify()

def packer2():
    while True:
        time.sleep(3)
        with condition_packer:
            while len(ovenBasket) < 3: 
                condition_packer.wait()

            if len(ovenBasket) >= 3 and not standSale.full():
                packed_item = f"[{ovenBasket.pop(0)}-{ovenBasket.pop(0)}-{ovenBasket.pop(0)}]"
                standSale.put(packed_item)
                print(f"📦 packer2 empacó: {packed_item}")
                time.sleep(3)

            if len(ovenBasket) < 3 and not standSale.full():
               condition_packer.notify() 
            
            with condition_sale:
                if standSale.full():
                    condition_sale.notify()

def salePerson(NCustomer: int):
    global sale_counter
    for _ in range(NCustomer):
        time.sleep(2)
        with condition_sale:
            while standSale.qsize() < 3:
                print("💰 salePerson entra en espera ...")
                condition_sale.wait()

            purchase_amount = random.randint(1, 3)
            items_sold = ""
            for _ in range(min(purchase_amount, standSale.qsize())):
                items_sold += standSale.get() + " ; "
                
            print(f"💰 Venta {sale_counter}: Se vendió {items_sold}")    
            sale_counter += 1
            time.sleep(2)

            if standSale.empty():
                condition_packer.notify_all() 
    
    with condition_sale:
        print("💰 salePerson finalizó las ventas.")
        os.kill(os.getpid(), signal.SIGUSR1)
        condition_sale.wait()
    

def main():
    """Función principal que maneja los hilos y los clientes."""
    NCustomer = 3  # Número de clientes simulados

    hilo_master = threading.Thread(target=bakerMaster, daemon=True)
    hilo_junior = threading.Thread(target=bakerJunior, daemon=True)
    hilo_baked = threading.Thread(target=bakedStep, daemon=True)
    hilo_packer1 = threading.Thread(target=packer1, daemon=True)
    hilo_packer2 = threading.Thread(target=packer2, daemon=True)
    hilo_sale = threading.Thread(target=salePerson, daemon=True, args=(NCustomer,))

    # Iniciar hilos
    hilo_master.start()
    hilo_junior.start()
    hilo_baked.start()
    hilo_packer1.start()
    hilo_packer2.start()
    hilo_sale.start()

    while not endAll:
        time.sleep(10)
        print(f"📌 Estado actual → mainTable: {len(mainTable)} | ovenBasket: {len(ovenBasket)} | standSale: {standSale.qsize()}")


    time.sleep(15)
if __name__ == "__main__":
    main()