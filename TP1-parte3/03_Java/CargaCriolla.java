import java.util.ArrayList;
import java.util.List;

public class CargaCriolla
{

    public static void main(String[] args)
    {

        if (args[0] == null || args[1] == null || args.length != 2)
        {
            System.out.println("error en parametros");
            return;
        }

        int trucksAmount = Integer.parseInt(args[0]);
        int travelsAmount = Integer.parseInt(args[1]);

        ShipmentsAdministrator shipmentsAdministrator = ShipmentsAdministrator.getShipmentsAdministrator(createTrucks(trucksAmount), createTravels(travelsAmount));

        shipmentsAdministrator.startAssigningTravels();

    }

    public static List<Truck> createTrucks(int trucksAmount)
    {
        List<Truck> trucks = new ArrayList<>();

        for (int i = 1; i <= trucksAmount; i++)
        {
            trucks.add(new Truck(i));
        }
        return trucks;
    }

    public static List<Travel> createTravels(int travelsAmount)
    {
        List<Plant> plants = List.of(new BsAsPlant(), new FernandezPlant(), new BsAsPlant());
        List<Travel> travels = new ArrayList<>();

        for (int i = 0; i < travelsAmount; i++)
        {
            travels.add(new Travel(plants));
        }
        return travels;
    }
}