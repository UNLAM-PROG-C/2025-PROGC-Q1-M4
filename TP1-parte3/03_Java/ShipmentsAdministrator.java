import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ShipmentsAdministrator
{
    private static ShipmentsAdministrator shipmentsAdministrator;

    List<Truck> trucks;
    List<Travel> travels;
    ExecutorService executor;
    CountDownLatch latch;

    public static ShipmentsAdministrator getShipmentsAdministrator(List<Truck> trucks, List<Travel> travels){
        if (shipmentsAdministrator == null){
            shipmentsAdministrator = new ShipmentsAdministrator(trucks, travels);
        }

        return shipmentsAdministrator;
    }

    private ShipmentsAdministrator(List<Truck> trucks, List<Travel> travels){
        this.trucks = trucks;
        this.travels = travels;
        this.executor = Executors.newFixedThreadPool(trucks.size());
        this.latch = new CountDownLatch(travels.size());

    }

    public void startAssigningTravels()
    {
        long initialTime = System.currentTimeMillis();
        while (!travels.isEmpty()){
            if(isAnyAvailableTruck())
            {
                Truck truck = findAvailableTruck();
                this.assignTravelToTruck(truck);
            }

        }
        System.out.println("No hay mas viajes");
        this.executor.shutdown();
        waitForTravelsToFinish();

        this.printTotalDays(initialTime, System.currentTimeMillis());
    }

    private void printTotalDays(long startTime, long finishTime){
        long hours = (finishTime - startTime)/Constants.SIMULATION_DELAY;
        Duration duration = Duration.ofHours(hours);
        long days = duration.toDays();
        System.out.println("Tiempo total: " + days +" dias");


    }

    private void waitForTravelsToFinish(){
        System.out.println("Esperando a que terminen los viajes en curso");
        try
        {
            latch.await();
        } catch (InterruptedException e)
        {
            System.out.println("Error mientras se finalizaban los viajes");
            throw new RuntimeException(e);
        }
        System.out.println("Todos los viajes finalizados");
    }

    private boolean isAnyTravelInCourse(){
        return this.trucks.stream().anyMatch(Truck::isTraveling);
    }

    private void assignTravelToTruck(Truck truck)
    {
        Travel travel = this.travels.remove(0);
        truck.assignTravel(travel);
        executor.submit(() ->
        {
            truck.run();
            latch.countDown();
        });
    }

    private boolean isAnyAvailableTruck(){
        return !this.trucks.stream().allMatch(Truck::isTraveling);
    }

    private Truck findAvailableTruck(){
        Optional<Truck> optTruck = trucks.stream().filter((t) -> !t.isTraveling()).findFirst();
        return optTruck.orElse(null);
    }
}
