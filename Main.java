package trip_planner;

import java.util.*;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static LinkedList<Town> plan = new LinkedList<>();

    public static void main(String[] args) {

        System.out.println("Welcome to trip planner!");
        boolean quit = false;
        while (!quit) {
            System.out.print("Choose an option:\n\t" +
                    "1 - add city to visit\n\t" +
                    "2 - remove city from the list\n\t" +
                    "3 - go trippin'\n\t" +
                    "0 - to quit\n" +
                    "Enter your choice [0-3]: ");

            int input = scanner.nextInt();
            scanner.nextLine();
            switch (input) {
                case 0 -> quit = true;
                case 1 -> {
                    Town town = addCity();
                    if (town == null){
                        System.out.println("Distance can't be negative.");
                        break;
                    }
                    if (!plan.contains(town)) {
                        if (sortByDistance(town) == plan.size()) {
                            plan.addLast(town);
                        } else {
                            plan.add(sortByDistance(town), town);
                        }
                        System.out.println(town.getTownName() + " has" +
                                " been added to the plan.\n********************");
                    } else {
                        if (town.getTownName().equals(plan.get(0).getTownName())) {
                            System.out.println(town.getTownName() + " is a starting point.");
                        } else {
                            System.out.println(town.getTownName() +
                                    " already on the list on position "
                                    + plan.indexOf(town) +
                                    "\nDistance from Sidney: " + town.getDistanceFromSidney() +
                                    "km.\n********************");
                        }
                    }
                }
                case 2 -> removeCity();
                case 3 -> {
                    if (plan.size() == 1){
                        System.out.println("You're at the starting point and no next city on list to be visited.");
                    }else {
                        goTripping();
                    }
                }
            }
        }
    }

    private static void goTripping() {
        boolean tripping = true;
        boolean forward = false;

        if (plan.isEmpty()) {
            System.out.println("Plan is empty - enter starting point and add cities");
        } else {

            ListIterator<Town> iterator = plan.listIterator();
            while (tripping) {
                int direction = direction();
                if (direction == 1) {
                    if (!iterator.hasPrevious()) {
                        System.out.print("Starting trip from " + iterator.next().getTownName());
                    } else if (iterator.hasNext()) {
                        if (!forward) {
                            iterator.next();
                        }
                        System.out.print("Tripping from " + iterator.previous().getTownName());
                        iterator.next();
                    }

                    if (iterator.hasNext()) {
                        System.out.println(" to " + iterator.next().getTownName());
                        forward = true;
                    }

                    if (!iterator.hasNext()) {
                        System.out.println("Final point " + iterator.previous().getTownName() +
                                " reached.\nPossible direction is backwards.");
                        iterator.next();
                    }

                } else if (direction == 2) {
                    if (!iterator.hasNext()) {
                        System.out.print("Tripping back from final point " + iterator.previous().getTownName());
                    } else if (iterator.hasPrevious()) {
                        if (!forward) {
                            iterator.next();
                        }
                        System.out.print("Tripping back from " + iterator.previous().getTownName());
                    }

                    if (iterator.hasPrevious()) {
                        System.out.println(" to " + iterator.previous().getTownName());
                        forward = false;
                    }

                    if (!iterator.hasPrevious()) {
                        System.out.println("Starting point " + iterator.next().getTownName() +
                                " reached.\nPossible direction is forward.");
                        iterator.previous();
                    }
                } else {
                    System.out.println("Getting back to menu");
                    tripping = false;
                }
            }
        }
    }

    public static int direction() {
        System.out.println("Options are:\t-(F)orward\t-(B)ackward" +
                "\t-(L)ist of places\t-(M)enu");

        String direction = scanner.nextLine();
        switch (direction.toLowerCase()) {
            case "f" -> {
                System.out.println("Going forward");
                return 1;
            }
            case "b" -> {
                System.out.println("Going backward");
                return 2;
            }
            case "m" -> {
                System.out.println("Returning to menu");
                return 3;
            }
            default -> {
                System.out.println("Improper choice.");
                direction();
            }
        }
        return -1;
    }


    private static void removeCity() {
        boolean onFile = false;
        System.out.print("*****************\nEnter city to be removed: ");
        String city = cityNameStd(scanner.nextLine());
        for (Town town : plan) {
            if (town.getTownName().toLowerCase().equals(city.toLowerCase())) {
                onFile = true;
                plan.remove(town);
                System.out.println(town.getTownName() + " removed from the list.");
                break;
            }
        }
        if (!onFile) {
            System.out.println(city + " not on file and can't be removed.");
        }
    }

    private static String cityNameStd(String string) {
        return string.toUpperCase().charAt(0) +
                string.toLowerCase().substring(1);
    }

    private static int sortByDistance(Town town) {
        ListIterator<Town> iterator = plan.listIterator();
        if (town.getDistanceFromSidney() >= plan.getLast().getDistanceFromSidney()) {
            return plan.size();
        } else {
            while (iterator.hasNext()) {
                iterator.next();
                if (town.getDistanceFromSidney() < iterator.next().getDistanceFromSidney()) {
                    return iterator.previousIndex();
                }
            }
        }
        return -1;
    }

    private static Town addCity() {
        if (plan.isEmpty()) {
            System.out.print("Enter city to be considered as starting point: ");
            String startingPoint = cityNameStd(scanner.nextLine());
            Town startingTown = new Town(startingPoint, 0);
            plan.add(0, startingTown);
            return startingTown;
        } else {
            System.out.print("Enter city to visit: ");
            String townName = cityNameStd(scanner.nextLine());
            int confirmation = onList(townName);
            if (confirmation < 0) {
                System.out.print("Enter distance: ");
                double distanceFromSidney = Double.parseDouble(scanner.nextLine());
                if (distanceFromSidney > 0) {
                    return new Town(townName, distanceFromSidney);
                }
                return null;
            }
            return plan.get(confirmation);
        }
    }

    private static int onList(String townName) {
        /*for (Town currentTown : plan) {
            if (currentTown.getTownName().equals(townName)) {
                return plan.indexOf(currentTown);
            }
        }*/
        ListIterator<Town> iterator = plan.listIterator();
        while (iterator.hasNext()) {
            Town currentTown = iterator.next();
            if (currentTown.getTownName().equals(townName)) {
                return plan.indexOf(currentTown);
            }
        }
        return -1;
    }


    private static class Town {
        private String townName;
        private double distanceFromSidney;

        public Town(String townName, double distanceFromSidney) {
            this.townName = townName;
            this.distanceFromSidney = distanceFromSidney;
        }

        public String getTownName() {
            return townName;
        }

        public double getDistanceFromSidney() {
            return distanceFromSidney;
        }
    }
}


