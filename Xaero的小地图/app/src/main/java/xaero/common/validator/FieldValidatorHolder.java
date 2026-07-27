package xaero.common.validator;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/validator/FieldValidatorHolder.class */
public class FieldValidatorHolder {
    private NumericFieldValidator numericFieldValidator;
    private WaypointCoordinateFieldValidator wpCoordFieldValidator;

    public FieldValidatorHolder(NumericFieldValidator numericFieldValidator, WaypointCoordinateFieldValidator wpCoordFieldValidator) {
        this.numericFieldValidator = numericFieldValidator;
        this.wpCoordFieldValidator = wpCoordFieldValidator;
    }

    public NumericFieldValidator getNumericFieldValidator() {
        return this.numericFieldValidator;
    }

    public WaypointCoordinateFieldValidator getWpCoordFieldValidator() {
        return this.wpCoordFieldValidator;
    }
}
