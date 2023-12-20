package com.example.converterapplab3;

import android.content.SharedPreferences;

import com.example.converterapplab3.services.BaseConvertor;
import com.example.converterapplab3.services.ConversionType;
import com.example.converterapplab3.services.LengthConverterService;
import com.example.converterapplab3.services.TemperatureConverterService;
import com.example.converterapplab3.services.WeightConverterService;

import java.util.concurrent.Callable;

public class ConvertorCallable implements Callable<String> {

    private ConversionType conversionType;
    private Double input;
    private String fromType;
    private String toType;
    private SharedPreferences sharedPreferences;
    BaseConvertor baseConvertor;
    public ConvertorCallable(ConversionType conversionType, Double input, String fromType, String toType, SharedPreferences sharedPreferences) {
        this.conversionType = conversionType;
        this.input = input;
        this.fromType = fromType;
        this.toType = toType;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public String call() throws Exception {

        if (conversionType == ConversionType.LENGTH) {
             baseConvertor = new LengthConverterService(sharedPreferences);
        } else if (conversionType == ConversionType.WEIGHT) {
            baseConvertor = new WeightConverterService(sharedPreferences);
        } else if (conversionType == ConversionType.TEMPERATURE) {
            baseConvertor = new TemperatureConverterService(sharedPreferences);
        }
        return formatter(baseConvertor.convert(input, fromType, toType));
    }

    public static String formatter(Double number) {
        if (number == null) {
            return null;
        }
        // Використовуємо метод String.format для форматування числа з точністю до двох десяткових знаків
        String formattedNumber = String.format("%.10f", number);

        // Видаляємо нулі, що знаходяться після двох десяткових знаків
        formattedNumber = formattedNumber.replaceAll("0*$", "");

        // Видаляємо крапку, якщо вона залишається на кінці
        formattedNumber = formattedNumber.replaceAll("\\.$", "");

        return formattedNumber;
    }
}
