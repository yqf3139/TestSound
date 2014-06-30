package seu.lab.dolphin.echo;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;
import android.view.MotionEvent;

/**
 * Created by yqf on 6/30/14.
 */
public class UltrasonicEmitter {
    private static final int sampleRate = 44100;
    private double freqOfTone; // hz
    boolean isEchoing = false;
    UltrasonicData data = null;
    private AudioTrack audioTrack;

    private static int division(int x, int y) {
        if (x < y) {
            int t = x;
            x = y;
            y = t;
        }
        while (y != 0) {
            if (x == y)
                return 1;
            else {
                int k = x % y;
                x = y;
                y = k;
            }
        }
        return x;
    }

    UltrasonicEmitter(double freqOfTone){
        this.freqOfTone = freqOfTone;
        data = genTone();
    }

    UltrasonicData genTone(){
        double wavelen;
        double max = 32768 ;
        double temp = 0;
        int tmp = 0;
        double a1;
        double a2;

        wavelen = sampleRate/freqOfTone;

        int numSamples = ((300 * (int) (1000 * wavelen)) / division(300, (int) (1000 * wavelen)));
        double sample[] = new double[numSamples];
        byte generatedSnd[] = new byte[2 * numSamples];
        try{
            for (int i = 0; i < numSamples; ++i) {
                temp = i / wavelen;
                tmp = (int) temp;
                a1 = temp-tmp;
                a2 = (i % wavelen) / wavelen;
                sample[i] = Math.sin(2 * Math.PI * a2);//
            }
            int idx = 0;
            for (final double dVal : sample) {
                // scale to maximum amplitude
                final short val = (short) ((dVal * max));
                // in 16 bit wav PCM, first byte is the low order byte
                generatedSnd[idx++] += (byte) (val & 0x00ff);
                generatedSnd[idx++] += (byte) ((val & 0xff00) >>> 8);
            }
        }catch(Exception e){
            Log.e("e", e.toString());
        }

        return new UltrasonicData(numSamples, generatedSnd);
    }


    boolean play(){
        if(isEchoing) return false;
        if(data == null) return false;
        audioTrack = new AudioTrack(
                AudioManager.STREAM_RING,
                sampleRate,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                data.numSamples,
                AudioTrack.MODE_STREAM);

        audioTrack.play();
        isEchoing = true;
        new Thread(){
            @Override
            public void run() {
            while (isEchoing)
                audioTrack.write(data.gen, 0, data.gen.length);
            }
        }.start();

        return true;
    }

    boolean stop(){
        if(!isEchoing) return false;
        isEchoing = false;
        audioTrack.stop();
        audioTrack.release();
        return true;
    }

}
