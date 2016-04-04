package com.lilsmile.borderonlineproductionedition.app;

/**
 * Created by Smile on 16.03.16.
 */
public class Classes {


    public class Continent
    {
        public int id;
        public boolean enable;
        public String name;

    }

    public class Border
    {
        public String additional_info;
        public String country_1;
        public String country_2;
        public int id;
    }

    public class Checkpoint
    {
        public int id;
        public String forward_locality;
        public String backward_locality;
        public Queue queue;

    }

    class Queue
    {
        public Source by_users;
        public Source official;
    }
    class Source
    {
        public int bus;
        public int truck;
        public int car;
    }

    public class BorderInfo
    {
        String name;
        String info;
    }


    public class CheckpointInfo
    {
        String shedule;
        int by_users_truck_queue_length;
        int official_truck_queue_time;
        int by_users_bus_queue_length;
        int forward_locality;
        int by_users_truck_queue_time;
        int official_car_queue_time;
        int by_users_car_queue_time;
        int id;
        int official_bus_queue_time;
        String description;
        int by_users_bus_queue_time;
        int backward_locality;
        String incidents;
        int by_users_car_queue_length;
    }

}
