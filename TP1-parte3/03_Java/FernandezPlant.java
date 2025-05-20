public class FernandezPlant extends Plant
{
    ChargeStation chargeStation;

    public FernandezPlant()
    {
        super("Fernandez", "Carbon", "Trigo");
        this.chargeStation = new ChargeStation();
    }

    @Override
    public void action(Truck truck) throws InterruptedException
    {
        super.action(truck);
        chargeStation.chargeTruck(truck);
    }
}
