import threading
import queue
import time
import random

# Listas y cola compartidas
mainTable = []
max_capacity_main = 20
ovenBasket = []
max_capacity_oven = 50
standSale = queue.Queue(maxsize=30)

# Bloqueos y condiciones
lock_main = threading.Lock()
condition_baked = threading.Condition(lock_main)
condition_packer = threading.Condition(lock_main)
condition_sale = threading.Condition(lock_main)

# Contadores de elementos generados
counter_master = 0
counter_junior = 0
sale_counter = 1  # Número de venta

def bakerMaster():
    global counter_master
    while True:
        time.sleep(4)
        with condition_baked:
            while len(mainTable) >= max_capacity_main:
                condition_baked.wait()
            mainTable.append(f"masaBM{counter_master}")
            counter_master += 1
            mainTable.append(f"masaBM{counter_master}")
            counter_master += 1
            print(f"🧑‍🍳 bakerMaster agregó: {mainTable[-2:]}")
            
            if len(mainTable) == max_capacity_main:
                condition_baked.notify()

def bakerJunior():
    global counter_junior
    while True:
        time.sleep(5)
        with condition_baked:
            while len(mainTable) >= max_capacity_main:
                condition_baked.wait()
            mainTable.append(f"masaBJ{counter_junior}")
            counter_junior += 1
            print(f"🧑‍🍳 bakerJunior agregó: {mainTable[-1]}")
            
            if len(mainTable) == max_capacity_main:
                condition_baked.notify()

def bakedStep():
    global ovenBasket
    while True:
        with condition_baked:
            while len(mainTable) < max_capacity_main:
                condition_baked.wait()
            
            while mainTable:
                with condition_packer:
                    if len(ovenBasket) >= max_capacity_oven:
                        condition_packer.wait()
                    
                    batch = [mainTable.pop(0) + 'H' for _ in range(min(3, len(mainTable)))]
                    time.sleep(6)
                    ovenBasket.extend(batch)
                    print(f"🔥 bakedStep movió al horno: {batch}")

                    if not mainTable:
                        condition_packer.notify_all()  # 🔥 Desbloquear `packer1` y `packer2`

def packer1():
    while True:
        with condition_packer:
            while len(ovenBasket) < max_capacity_oven:
                condition_packer.wait()

            while ovenBasket and not standSale.full():
                packed_item = f"[{ovenBasket.pop(0)}-{ovenBasket.pop(0)}-{ovenBasket.pop(0)}]"
                standSale.put(packed_item)
                print(f"📦 packer1 empacó: {packed_item}")
                time.sleep(3)
            
            if standSale.full():
                condition_sale.notify()

def packer2():
    while True:
        with condition_packer:
            while len(ovenBasket) < max_capacity_oven:
                condition_packer.wait()

            while ovenBasket and not standSale.full():
                packed_item = f"[{ovenBasket.pop(0)}-{ovenBasket.pop(0)}-{ovenBasket.pop(0)}]"
                standSale.put(packed_item)
                print(f"📦 packer2 empacó: {packed_item}")
                time.sleep(3)
            
            if standSale.full():
                condition_sale.notify()

def salePerson():
    global sale_counter
    while True:
        with condition_sale:
            while standSale.qsize() < 3:
                condition_sale.wait()

            purchase_amount = random.randint(1, 3)
            for _ in range(min(purchase_amount, standSale.qsize())):
                item_sold = standSale.get()
                print(f"💰 Venta {sale_counter}: Se vendió {item_sold}")
                sale_counter += 1
            
            time.sleep(2)

            if standSale.empty():
                condition_packer.notify_all()  # 🔥 Reactivar `packer1` y `packer2`

def main():
    """Función principal que maneja los hilos y los clientes."""
    N = 3  # Número de clientes simulados

    hilo_master = threading.Thread(target=bakerMaster, daemon=True)
    hilo_junior = threading.Thread(target=bakerJunior, daemon=True)
    hilo_baked = threading.Thread(target=bakedStep, daemon=True)
    hilo_packer1 = threading.Thread(target=packer1, daemon=True)
    hilo_packer2 = threading.Thread(target=packer2, daemon=True)
    hilo_sale = threading.Thread(target=salePerson, daemon=True)

    # Iniciar hilos
    hilo_master.start()
    hilo_junior.start()
    hilo_baked.start()
    hilo_packer1.start()
    hilo_packer2.start()
    hilo_sale.start()

    while True:
        time.sleep(10)
        print(f"📌 Estado actual → mainTable: {len(mainTable)} | ovenBasket: {len(ovenBasket)} | standSale: {standSale.qsize()}")

        with condition_packer:
            if len(ovenBasket) >= max_capacity_oven:
                condition_packer.wait()

if __name__ == "__main__":
    main()