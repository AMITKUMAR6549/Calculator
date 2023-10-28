package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    private boolean lastNumeric = false;
    private boolean lastDot = false;
    private TextView tvInput;
    private double lastResult = 0;

    private boolean lastCalculation = false; // Flag to track if the last calculation was completed


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInput = findViewById(R.id.tvInput);
    }

    public void onDigit(View view) {
        if (lastCalculation) {
            // If the last operation was completed, start a new calculation
            tvInput.setText("");
            lastCalculation = false; // Reset the flag
        }
        tvInput.append(((Button) view).getText());
        lastNumeric = true;
    }

    public void onClear(View view) {
        tvInput.setText("");
        lastNumeric = false;
        lastDot = false;
    }

    public void onDecimalPoint(View view) {
        if (lastNumeric && !lastDot) {
            tvInput.append(".");
            lastNumeric = false;
            lastDot = true;
        }
    }

    public void onOperator(View view) {
        String input = tvInput.getText().toString().trim();

        char lastChar = input.isEmpty() ? ' ' : input.charAt(input.length() - 1);

        if (isOperator(lastChar)) {
            // If the last character is an operator, replace it with the new operator
            input = input.substring(0, input.length() - 1) + ((Button) view).getText();
        } else {
            // Append the new operator
            input = input + ((Button) view).getText();
        }

        tvInput.setText(input);
        lastNumeric = false;
        lastDot = false;
        lastCalculation = false; // Reset the lastCalculation flag
    }









    private boolean isOperator(char c) {
        return c == '/' || c == '*' || c == '-' || c == '+';
    }




    public void onEqual(View view) {
        if (lastNumeric) {
            String tvValue = tvInput.getText().toString();
            try {
                Expression expression = new ExpressionBuilder(tvValue).build();
                lastResult = expression.evaluate();
                tvInput.setText(removeZeroAfterDot(String.valueOf(lastResult)));
                lastNumeric = true;
                lastDot = tvInput.getText().toString().contains(".");
                lastCalculation = true;
            } catch (ArithmeticException e) {
                tvInput.setText("Error");
                e.printStackTrace();
            } catch (Exception e) {
                tvInput.setText("Error");
                e.printStackTrace();
            }
        } else {
            tvInput.setText("Error");
        }
    }









    public void onPercentage(View view) {
        if (lastNumeric) {
            String input = tvInput.getText().toString();
            double number = Double.parseDouble(input);
            double percentage = number / 100;
            tvInput.setText(String.valueOf(percentage));
            lastDot = true; // Allow decimal point after percentage calculation
        }
    }

    public void onDoubleZero(View view) {
        tvInput.append("00");
        lastNumeric = true;
        lastDot = false;
    }

    public void onBackspace(View view) {
        String currentInput = tvInput.getText().toString();
        if (currentInput.length() > 0) {
            // Remove the last character from the input
            tvInput.setText(currentInput.substring(0, currentInput.length() - 1));
            // Check if the last removed character was a numeric or decimal point
            lastNumeric = !(currentInput.charAt(currentInput.length() - 1) == '.');
            lastDot = !(currentInput.contains("."));
        }
    }

    private String removeZeroAfterDot(String result) {
        String value = result;
        if (result.contains(".0")) {
            value = result.substring(0, result.length() - 2);
        }
        return value;
    }

    private boolean isOperatorAdded(String value) {
        return !(value.isEmpty()) && (value.contains("/")
                || value.contains("*")
                || value.contains("-")
                || value.contains("+")
                || value.contains("%"));
    }
}
