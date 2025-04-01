package com.madnessman666.xoroboomtamer;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    ConsumerIrManager mCIR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCIR = (ConsumerIrManager) getSystemService(Context.CONSUMER_IR_SERVICE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public static String hexToBin(String hex){
        hex = hex.replaceAll("0", "0000");
        hex = hex.replaceAll("1", "0001");
        hex = hex.replaceAll("2", "0010");
        hex = hex.replaceAll("3", "0011");
        hex = hex.replaceAll("4", "0100");
        hex = hex.replaceAll("5", "0101");
        hex = hex.replaceAll("6", "0110");
        hex = hex.replaceAll("7", "0111");
        hex = hex.replaceAll("8", "1000");
        hex = hex.replaceAll("9", "1001");
        hex = hex.replaceAll("A", "1010");
        hex = hex.replaceAll("B", "1011");
        hex = hex.replaceAll("C", "1100");
        hex = hex.replaceAll("D", "1101");
        hex = hex.replaceAll("E", "1110");
        hex = hex.replaceAll("F", "1111");
        return hex;
    };
    public static int[] sendNECCode(String hex){
        int[] pattern = new int[68];
        int offset = 2;
        String bytes = hexToBin(hex);
        pattern[0] = 9000;
        pattern[1] = 4500;
        for (char b: bytes.toCharArray()) {
            if(b == '0'){
                pattern[offset] = 560;
                pattern[offset + 1] = 560;
                offset += 2;
            } else if(b == '1'){
                pattern[offset] = 560;
                pattern[offset + 1] = 1690;
                offset += 2;
            }
        }
        pattern[66] = 560;
        pattern[67] = 560;
        return pattern;
    };
    public void powerOn(View view){
        Log.i("Fuck", "Sending powe on signal...");
        String hexCode = "02FD18E7";

        int carrierFrequency = 38000; // 38.4kHz - стандартная частота для NEC
        int[] patternFullCode_MSB_first_RawBytes = {
                // PREAMBLE (Стартовый бит)
                9000, 4500,

                // Байт 1: 00000010 (HEX: 02)
                560,560, 560,560, 560,560, 560,560, 560,560, 560,560, 560,1690, 560,560,
                // Байт 2: 11111101 (HEX: FD)
                560,1690, 560,1690, 560,1690, 560,1690, 560,1690, 560,1690, 560,560, 560,1690,
                // Байт 3: 00011000 (HEX: 18)
                560,560, 560,560, 560,560, 560,1690, 560,1690, 560,560, 560,560, 560,560,
                // Байт 4: 11100111 (HEX: E7)
                560,1690, 560,1690, 560,1690, 560,560, 560,560, 560,1690, 560,1690, 560,1690,
                560,560 //END
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // mCIR.transmit(carrierFrequency, patternFullCode_MSB_first_RawBytes);
            mCIR.transmit(carrierFrequency, sendNECCode("02FD18E7"));
            //mCIR.transmit(carrierFrequency, sendNECCode("02FD30CF"));
            //Toast.makeText(this, "ИК-команда отправлена (пример)", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Отправка ИК-сигнала поддерживается только с Android 4.4 (KitKat) и выше", Toast.LENGTH_SHORT).show();
        }
    }
    }