import java.util.List;

public class Travel
{
    List<Plant> plants;

    public Travel(List<Plant> plants){
        this.plants = plants;
    }

    public Plant getNextStop(Plant currentPlant){
        int nextStopIndex = this.plants.indexOf(currentPlant) + 1;

        if(nextStopIndex < plants.size()){
            return plants.get(nextStopIndex);
        }

        return null;
    }

    public Plant getInitialStop(){
        return this.plants.get(0);
    }
}
