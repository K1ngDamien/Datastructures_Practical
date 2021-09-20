package model;

public class Shunter {


    /* four helper methods than are used in other methods in this class to do checks */
    private static boolean isSuitableWagon(Train train, Wagon wagon) {
        // trains can only exist of passenger wagons or of freight wagons

        // If the train has wagons, then only wagons of the same type can be attached. If not, then a wagon can always be attached.
        if (!train.hasNoWagons()) {
            Class wagonType = wagon.getClass();
            Class trainType = train.getFirstWagon().getClass();

            return wagonType == trainType;
        }

        return true;

    }

    private static boolean isSuitableWagon(Wagon one, Wagon two) {
        // passenger wagons can only be hooked onto passenger wagons

        Class wagonOneInstance = one.getClass();
        Class wagonTwoInstance = two.getClass();

        return wagonOneInstance == wagonTwoInstance;
    }

    private static boolean hasPlaceForWagons(Train train, Wagon wagon) {
        // the engine of a train has a maximum capacity, this method checks for a ro of wagons
        int maxWagons = train.getEngine().getMaxWagons();
        int attachedWagonsBehindTrain = 0;

        if (train.getFirstWagon() != null) {
            attachedWagonsBehindTrain = train.getFirstWagon().getNumberOfWagonsAttached() + 1;
        }

        int totalAttachedWagonsBehindWagon = wagon.getNumberOfWagonsAttached() + 1;
        int newTrainSize = attachedWagonsBehindTrain + totalAttachedWagonsBehindWagon;

        return newTrainSize <= maxWagons;
    }

    private static boolean hasPlaceForOneWagon(Train train, Wagon wagon) {
        // the engine of a train has a maximum capacity, this method checks for one wagon
        int maxWagons = train.getEngine().getMaxWagons();
        int attachedWagons = 0;

        return train.getFirstWagon().getNumberOfWagonsAttached() + 1 == maxWagons;
    }

    public static boolean hookWagonOnTrainRear(Train train, Wagon wagon) {
         /* check if Locomotive can pull new number of Wagons
         check if wagon is correct kind of wagon for train
         find the last wagon of the train
         hook the wagon on the last wagon (see Wagon class)
         adjust number of Wagons of Train */

         if(train.getFirstWagon() == null) {
             train.setFirstWagon(wagon);

             return true;
         } else {

             if(isSuitableWagon(train, wagon) && hasPlaceForWagons(train, wagon)) {
                 Wagon finalWagon = train.getFirstWagon().getLastWagonAttached();
                 finalWagon.setPreviousWagon(wagon);
                 wagon.setNextWagon(finalWagon);
                 train.resetNumberOfWagons();

                 return true;
             }

         }

         return false;

    }

    public static boolean hookWagonOnTrainFront(Train train, Wagon wagon) {
        /* check if Locomotive can pull new number of Wagons
         check if wagon is correct kind of wagon for train
         if Train has no wagons hookOn to Locomotive
         if Train has wagons hookOn to Locomotive and hook firstWagon of Train to lastWagon attached to the wagon
         adjust number of Wagons of Train */

        if (!train.hasNoWagons() && isSuitableWagon(train, wagon) && hasPlaceForWagons(train, wagon)){
            Wagon previousFirstWagon = train.getFirstWagon();

            train.setFirstWagon(wagon);
            wagon.setPreviousWagon(previousFirstWagon);
            train.resetNumberOfWagons();

        } else {
            // Train has no wagons, thus perform action...
            train.setFirstWagon(wagon);
        }
        return true;
    }

    public static boolean hookWagonOnWagon(Wagon first, Wagon second) {
        /* check if wagons are of the same kind (suitable)
        * if so make second wagon next wagon of first */

        if (isSuitableWagon(first, second)) {
            first.setPreviousWagon(second);

            return true;
        }

        return false;

    }


    public static boolean detachAllFromTrain(Train train, Wagon wagon) {
        /* check if wagon is on the train
         detach the wagon from its previousWagon with all its successor
         recalculate the number of wagons of the train */

        if (train.getPositionOfWagon(wagon.getWagonId()) != -1) {
            Wagon nextWagon = wagon.getNextWagon();

            if (nextWagon != null) {
                nextWagon.setPreviousWagon(null);
            }

            train.resetNumberOfWagons();

            return true;
        }

        return false;

    }

    public static boolean detachOneWagon(Train train, Wagon wagon) {
        /* check if wagon is on the train
         detach the wagon from its previousWagon and hook the nextWagon to the previousWagon
         so, in fact remove the one wagon from the train
        */
        Wagon toBeDetachedWagon = train.getWagonOnPosition(train.getPositionOfWagon(wagon.getWagonId()));
        Wagon previousWagon = toBeDetachedWagon.getPreviousWagon();
        Wagon nextWagon = toBeDetachedWagon.getNextWagon();

        if ( previousWagon != null && nextWagon != null ) {
            nextWagon.setPreviousWagon(previousWagon);
            previousWagon.setNextWagon(nextWagon);
        } else if(nextWagon != null) {
            nextWagon.setPreviousWagon(null);
        } else {
            train.setFirstWagon(previousWagon);
        }

        if (toBeDetachedWagon.hasPreviousWagon()) {
            toBeDetachedWagon.setPreviousWagon(null);
        }

        if(toBeDetachedWagon.hasNextWagon()) {
            toBeDetachedWagon.setNextWagon(null);
        }

        return false;

    }

    public static boolean moveAllFromTrain(Train from, Train to, Wagon wagon) {
        /* check if wagon is on train from <-- done
         check if wagon is correct for train and if engine can handle new wagons <-- done
         detach Wagon and all successors from train from and hook at the rear of train to
         remember to adjust number of wagons of trains */

        int wagonPosition = from.getPositionOfWagon(wagon.getWagonId());

        // Wagon is on position if the position is not set to -1
        if (wagonPosition != -1 && isSuitableWagon(to, wagon) && hasPlaceForWagons(to, wagon)){
            //Detach wagon and all successors from train from
            detachAllFromTrain(from, wagon);
            hookWagonOnTrainRear(to, wagon);

            return true;
        }

        return false;
    }

    public static boolean moveOneWagon(Train from, Train to, Wagon wagon) {
        // detach only one wagon from train from and hook on rear of train to
        // do necessary checks and adjustments to trains and wagon

        int wagonPosition = from.getPositionOfWagon(wagon.getWagonId());

        if (wagonPosition != -1 && isSuitableWagon(to, wagon) && hasPlaceForWagons(to, wagon)){
            //Detach one wagon and all successors from train from
            detachOneWagon(from, wagon);
            hookWagonOnTrainRear(to, wagon);

            to.resetNumberOfWagons();
            from.resetNumberOfWagons();

            return true;
        }

        return false;

    }
}
