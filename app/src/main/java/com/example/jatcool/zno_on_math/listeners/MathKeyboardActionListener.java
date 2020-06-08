package com.example.jatcool.zno_on_math.listeners;

import android.inputmethodservice.KeyboardView;
import android.util.Log;
import android.widget.EditText;

public class MathKeyboardActionListener implements KeyboardView.OnKeyboardActionListener {
    private EditText editText;

    public MathKeyboardActionListener() {
    }

    public MathKeyboardActionListener(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        //Here check the primaryCode to see which key is pressed
        //based on the android:codes property
        if (primaryCode == 1) {
            Log.i("Key", "You just pressed 1 button");
        }

        String pasteText = " ";

        switch (primaryCode) {
            case 0: {
                pasteText = "$$Введіть формулу$$";
                break;
            }
            case 1: {
                pasteText = "^{введіть ступінь}";
                break;
            }
            case 2: {
                pasteText = "_{введіть індекс}";
                break;
            }
            case 3: {
                pasteText = "\\infty";
                break;
            }
            case 4: {
                pasteText = "\\left";
                break;
            }
            case 5: {
                pasteText = "\\right";
                break;
            }
            case 6: {
                pasteText = "\\frac{введіть чисельник}{введіть знаменник}";
                break;
            }
            case 7: {
                pasteText = "\\sqrt[введіть ступень кореня] {введіть підкореневий вираз}";
                break;
            }
            case 8: {
                pasteText = "\\int_{введіть нижню границю інтеграла}^{введіть верхню границю інтеграла} {введіть підінтегральний вираз}";
                break;
            }
            case 9: {
                pasteText = "\\sum_{введіть нижню границю суми}^{введіть верхню границю суми} {введіть вираз}";
                break;
            }
            case 10: {
                pasteText = "\\lim_{введіть індекс границі } {введіть вираз}";
                break;
            }
            case 11: {
                pasteText = "\\to";
                break;
            }
            case 12: {
                pasteText = "\\log_{введіть основу логарифма} {введіть вираз}";
                break;
            }
            case 13: {
                pasteText = "\\sin {введіть вираз}";
                break;
            }
            case 14: {
                pasteText = "\\cos {введіть вираз}";
                break;
            }
            case 15: {
                pasteText = "\\tan {введіть вираз}";
                break;
            }
            case 16: {
                pasteText = "\\cot {введіть вираз}";
                break;
            }
            case 17: {
                pasteText = "\\arcsin {введіть вираз}";
                break;
            }
            case 18: {
                pasteText = "\\arccos {введіть вираз}";
                break;
            }
            case 19: {
                pasteText = "\\arctan {введіть вираз}";
                break;
            }
            case 20: {
                pasteText = "\\rm arccot {введіть вираз}";
                break;
            }
            case 21: {
                pasteText = "^\\circ";
                break;
            }
            case 22: {
                pasteText = "\\pi";
                break;
            }
            case 23: {
                pasteText = "\\begin{cases}Введіть систему рівняннь використовуючи \u21B5 для розділення\\end{cases}";
                break;
            }
            case 24: {
                pasteText = "\\";
            }
        }

        int start = editText.getSelectionStart();
        editText.getText().insert(start, pasteText);
    }

    @Override
    public void onPress(int arg0) {
        //String output="\\u"+arg0;
        //output=output.replace("\\\\","\\");


    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onText(CharSequence text) {
        //txtTextQuestion.setText(txtTextQuestion.getText().toString() +text);
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }
}
