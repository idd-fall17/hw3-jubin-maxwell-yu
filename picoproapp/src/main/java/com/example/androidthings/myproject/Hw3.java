package com.example.androidthings.myproject;

import android.util.Log;

import java.io.IOException;

import com.google.android.things.contrib.driver.mma8451q.Mma8451Q;
import android.os.Handler;
import com.google.android.things.pio.Gpio;

/**
 * HW3 Template
 * Created by bjoern on 9/12/17.
 * Wiring:
 * USB-Serial Cable:
 *   GND to GND on IDD Hat
 *   Orange (Tx) to UART6 RXD on IDD Hat
 *   Yellow (Rx) to UART6 TXD on IDD Hat
 * Accelerometer:
 *   Vin to 3V3 on IDD Hat
 *   GND to GND on IDD Hat
 *   SCL to SCL on IDD Hat
 *   SDA to SDA on IDD Hat
 * Analog sensors:
 *   Middle of voltage divider to Analog A0..A3 on IDD Hat
 */

public class Hw3 extends SimplePicoPro {

    Mma8451Q accelerometer;

    float[] xyz = {0.f,0.f,0.f}; //store X,Y,Z acceleration of MMA8451 accelerometer here [units: G]
    float a0,a1,a2,a3; //store analog readings from ADS1015 ADC here [units: V]

    public void setup() {

        // Initialize the serial port for communicating to a PC
        uartInit(UART6,9600);

        pinMode(GPIO_32,Gpio.DIRECTION_IN);

        // Initialize the Analog-to-Digital converter on the HAT
        //analogInit(); //need to call this first before calling analogRead()

        // Initialize the MMQ8451 Accelerometer
        try {
            accelerometer = new Mma8451Q("I2C1");
            accelerometer.setMode(Mma8451Q.MODE_ACTIVE);
        } catch (IOException e) {
            Log.e("HW3Template","setup",e);
        }
    }

    String tracker = "";
    float[] caliber = {0.f,0.f,0.f};

    public void loop() {
        // read all analog channels and print to UART
        /*a0 = analogRead(A0);
        a1 = analogRead(A1);
        a2 = analogRead(A2);
        a3 = analogRead(A3);
        println(UART6,"A0: "+a0+"   A1: "+a1+"   A2: "+a2+"   A3: "+a3); // this goes to the Serial port
        println("A0: "+a0+"   A1: "+a1+"   A2: "+a2+"   A3: "+a3); // this goes to the Android Monitor in Android Studio
*/

        // read I2C accelerometer and print to UART

        try {
            xyz = accelerometer.readSample();
            //println(UART6,"X: "+xyz[0]+"   Y: "+xyz[1]+"   Z: "+xyz[2]);
            println("X: "+xyz[0]+"   Y: "+xyz[1]+"   Z: "+xyz[2]);

            if(digitalRead(GPIO_32) == LOW) {
                caliber[0] = xyz[0];
                caliber[1] = xyz[1];
                caliber[2] = xyz[2];
            }else if (xyz[0]-caliber[0]>0.3){
                println(UART6,"Left");
            }else if (xyz[0]-caliber[0]<-0.3){
                println(UART6,"Right");
            }else if (xyz[1]-caliber[1]<-0.3) {
                println(UART6,"Up");
            }else if (xyz[1]-caliber[1]>0.3){
                println(UART6,"Down");
            }else{
                println(UART6,"None");
            }

            println(caliber[0] + "    " + caliber[1] + "    " + caliber[2]);



            //use this line instead for unlabeled numbers separated by tabs that work with Arduino's SerialPlotter:
            //println(UART6,xyz[0]+"\t"+xyz[1]+"\t"+xyz[2]); // this goes to the Serial port

        } catch (IOException e) {
            Log.e("HW3Template","loop",e);
        }


        /*
        try {
            xyz = accelerometer.readSample();
            //println(UART6,"X: "+xyz[0]+"   Y: "+xyz[1]+"   Z: "+xyz[2]);
            println("X: "+xyz[0]+"   Y: "+xyz[1]+"   Z: "+xyz[2]);

            if(digitalRead(GPIO_32) == LOW) {
                caliber[0] = xyz[0];
                caliber[1] = xyz[1];
                caliber[2] = xyz[2];
            }else if (tracker=="Up") {
                //println(UART6,"Up");
            }else if (xyz[0]-caliber[0]>0.5){
                println(UART6,"Left");
            }else if (xyz[0]-caliber[0]<-0.5){
                println(UART6,"Right");
            }else if (xyz[2]-caliber[2]<-0.5 && xyz[1]-caliber[1]<0.7) {
                println(UART6, "Up");


                tracker = "Up";

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {

                        tracker = "";
                    }
                }, 1000);
            }else if (xyz[1]-caliber[1]>0.3){
                println(UART6,"Down");
            }else{
                println(UART6,"None");
            }

            println(caliber[0] + "    " + caliber[1] + "    " + caliber[2]);



            //use this line instead for unlabeled numbers separated by tabs that work with Arduino's SerialPlotter:
            //println(UART6,xyz[0]+"\t"+xyz[1]+"\t"+xyz[2]); // this goes to the Serial port

        } catch (IOException e) {
            Log.e("HW3Template","loop",e);
        }

        */


        delay(40);

    }
}
