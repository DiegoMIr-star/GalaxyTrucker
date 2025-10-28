package model.DifferentShipComponents;


import java.io.Serializable;

/**
 * This enumeration is the representation of the different kind of possible sides
 * in a component
 */
public enum Side implements Serializable {
    SingleConnector,
    DoubleConnector, UniversalConnector,
    BlankSide, CannonSpace, MotorSpace,
    ShieldProtection,
    ShieldAndSingleConnector, ShieldAndDoubleConnector, ShieldAndUniversalConnector
}