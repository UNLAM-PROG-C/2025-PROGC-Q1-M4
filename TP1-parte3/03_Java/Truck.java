public class Truck implements Runnable
{

    private boolean isLoaded;

    int number;
    Plant currentPlant;
    Travel travel;

    Driver driver;

    public Truck(int number)
    {
        this.number = number;
        this.isLoaded = false;
    }

    public void assignTravel(Travel travel)
    {
        this.travel = travel;
        this.driver = new Driver();
        this.currentPlant = travel.getInitialStop();
    }

    public void goToNextStop() throws InterruptedException
    {
        System.out.println("Camion "+this.number+" sale de "+this.currentPlant.getName());
        int travelTime = Math.round(this.driver.getTravelTime()* Constants.SIMULATION_DELAY);
        Thread.sleep(travelTime);
        this.currentPlant = travel.getNextStop(currentPlant);
        System.out.format("Camion "+this.number+" llega a "+ currentPlant.getName()+ "(%.2f hs de viaje)%n",travelTime/(float)Constants.SIMULATION_DELAY);

    }

    public boolean isLoaded()
    {
        return this.isLoaded;
    }

    public void unload() throws InterruptedException
    {
        Thread.sleep(Constants.LOAD_OR_UNLOAD_TIME * Constants.SIMULATION_DELAY);
        this.isLoaded = false;
        System.out.println(this + " descargado");
    }

    public void load() throws InterruptedException
    {
        Thread.sleep(Constants.LOAD_OR_UNLOAD_TIME * Constants.SIMULATION_DELAY);
        System.out.println(this + " cargado");
        this.isLoaded = true;
    }

    @Override
    public void run()
    {
        System.out.println(this + " iniciando viaje");
        try{
            this.currentPlant.action(this);
            while (travel.getNextStop(this.currentPlant) != null){
                this.goToNextStop();
                this.currentPlant.action(this);
            }
            System.out.println(this + " finalizo viaje");
            this.travel = null;
        }catch (InterruptedException e){
            System.out.println("Error en hilo de camion "+this.number);
        }
    }

    public boolean isTraveling(){
        return travel != null;
    }

    @Override
    public String toString()
    {
        return "Camion " + number;
    }
}
