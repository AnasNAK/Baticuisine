package org.NAK.entities;

import java.util.UUID;

public class Material extends Component{

    private double TransportCost;

    public Material(){
        super();
    }

    public double getTransportCost() {
        return TransportCost;
    }

    public void setTransportCost(double transportCost) {
        TransportCost = transportCost;
    }
}
