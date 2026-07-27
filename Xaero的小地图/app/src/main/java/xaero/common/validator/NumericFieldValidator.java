package xaero.common.validator;

import net.minecraft.client.gui.components.EditBox;

/* loaded from: [Xaero的小地图] xaerominimap-neoforge-1.21.1-25.3.10.jar:xaero/common/validator/NumericFieldValidator.class */
public class NumericFieldValidator {
    protected StringBuilder stringBuilder = new StringBuilder();

    protected boolean charIsValid(char c, int index) {
        return (c >= '0' && c <= '9') || (c == '-' && index == 0);
    }

    public void validate(EditBox field) {
        String text = field.getValue();
        char[] charArray = text.toCharArray();
        this.stringBuilder.delete(0, this.stringBuilder.length());
        boolean validated = true;
        for (int i = 0; i < charArray.length; i++) {
            if (!charIsValid(charArray[i], i)) {
                validated = false;
            } else {
                this.stringBuilder.append(charArray[i]);
            }
        }
        if (!checkNumberFormat(validated)) {
            field.setValue(this.stringBuilder.toString());
        }
    }

    protected boolean checkNumberFormat(boolean validated) throws NumberFormatException {
        boolean validFormat = false;
        while (!validFormat) {
            try {
                if (this.stringBuilder.length() != 0 && (this.stringBuilder.length() != 1 || this.stringBuilder.charAt(0) != '-')) {
                    Integer.parseInt(this.stringBuilder.toString());
                }
                validFormat = true;
            } catch (NumberFormatException e) {
                this.stringBuilder.deleteCharAt(this.stringBuilder.length() - 1);
                validated = false;
            }
        }
        return validated;
    }
}
