package seu.lab.dolphin.echo;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.util.Log;
import android.widget.Toast;

import com.example.app.R;

/**
 * Created by yqf on 6/30/14.
 */
public class UltrasonicManager {

    private static AudioManager audioManager = null;
    public static Context context;
    public final static double[] freqTable = new double[]{

    };

    double[] testTable = {
            2.1,2.109,
            2.138,2.148,2.158,2.168,2.178,2.188,2.198,
            2.306,2.352,2.364,2.376,2.388,
            2.425,
            2.45
            //,2.34,2.284,,2.412,,2.291
    };

    public final static double[] wavelenTable = new double[]{
            2.45,
            2.388,
            2.306,
            2.198,
            2.138,
            2.1
    };

    static UltrasonicEmitter emitters[] = new UltrasonicEmitter[wavelenTable.length];

    public static UltrasonicManager getUltrasonicManager(AudioManager audioManager) {

        UltrasonicManager.audioManager = audioManager;
        return new UltrasonicManager();
    }

    public static boolean switchToEarphoneSpeaker(){
        if(audioManager == null) return false;
        audioManager.setSpeakerphoneOn(false);//关闭扬声器
        audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);//MODE_IN_CALL
        audioManager.setParameters("noise_suppression=off");
        //audioManager.playSoundEffect();
        return true;
    }

    public static boolean switchToLoudspeaker(){
        if(audioManager == null) return false;
        audioManager.setSpeakerphoneOn(true);
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setParameters("noise_suppression=off");
        return true;
    }

    private UltrasonicManager(){}

    public boolean emit(int freqIndex){
        if(freqIndex < 0 || freqIndex >= wavelenTable.length) return false;
        emitters[freqIndex] = emitters[freqIndex] == null
                ? new UltrasonicEmitter(0,wavelenTable[freqIndex])
                : emitters[freqIndex];

        return emitters[freqIndex].play();
    }


    public void test(){
        new Thread(){
            @Override
            public void run() {
                UltrasonicEmitter emitter = null;
                for (int i = 0; i < testTable.length; i++){
                    double aa = testTable[i];
                    //aa /= 1000;
                    Log.e("emitter",""+aa);
                    emitter = new UltrasonicEmitter(0,aa);
                    emitter.play();
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    emitter.stop();
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }

    public boolean kill(int freqIndex){
        if(freqIndex < 0 || freqIndex >= wavelenTable.length) return false;
        if(emitters[freqIndex] == null) return false;
        if(!emitters[freqIndex].isEchoing) return false;
        return emitters[freqIndex].stop();
    }

}
