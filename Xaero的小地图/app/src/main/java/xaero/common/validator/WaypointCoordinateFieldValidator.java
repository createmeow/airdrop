package xaero.common.validator;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/validator/WaypointCoordinateFieldValidator.class */
public class WaypointCoordinateFieldValidator extends NumericFieldValidator {
    @Override // xaero.common.validator.NumericFieldValidator
    protected boolean charIsValid(char c, int index) {
        return (c == '~' && index == 0) || (!this.stringBuilder.toString().equals("~") && super.charIsValid(c, index));
    }

    @Override // xaero.common.validator.NumericFieldValidator
    protected boolean checkNumberFormat(boolean validated) {
        if (this.stringBuilder.toString().equals("~")) {
            return validated;
        }
        return super.checkNumberFormat(validated);
    }
}
