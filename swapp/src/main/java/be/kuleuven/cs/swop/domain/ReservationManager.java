package be.kuleuven.cs.swop.domain;


import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import be.kuleuven.cs.swop.domain.resource.Reservation;


public class ReservationManager {

    private Set<Reservation> reservations = new HashSet<Reservation>();

    public ReservationManager() {}

    public Set<Reservation> getReservations() {
        return ImmutableSet.copyOf(reservations);
    }
    
    protected boolean canHaveAsReservation(Reservation reservation){
        return reservation != null;
    }
    public void addProject(Reservation reservation) {
        if (!canHaveAsReservation(reservation)) throw new IllegalArgumentException(ERROR_ILLEGAL_RESERVATION);
        reservations.add(reservation);
    }
    
    private static String ERROR_ILLEGAL_RESERVATION = "Illegal reservation in reservation manager.";
}
