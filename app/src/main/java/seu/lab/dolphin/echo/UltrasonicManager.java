package seu.lab.dolphin.echo;

import android.media.AudioManager;

/**
 * Created by yqf on 6/30/14.
 */
public class UltrasonicManager {

    private static AudioManager audioManager = null;

    public final static double[] freqTable = new double[]{
//            16800,
//            17640,
            18000,
            19600,
//            20000,
            21000,
    };

    static UltrasonicEmitter emitters[] = new UltrasonicEmitter[freqTable.length];

    public static UltrasonicManager getUltrasonicManager(AudioManager audioManager) {
        UltrasonicManager.audioManager = audioManager;
        return new UltrasonicManager();
    }

    public static boolean switchToEarphoneSpeaker(){
        if(audioManager == null) return false;
        audioManager.setSpeakerphoneOn(false);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        return true;
    }

    public static boolean switchToLoudspeaker(){
        if(audioManager == null) return false;
        audioManager.setSpeakerphoneOn(true);
        audioManager.setMode(AudioManager.MODE_NORMAL);
        return true;
    }

    private UltrasonicManager(){}

    public boolean emit(int freqIndex){
        if(freqIndex < 0 || freqIndex >= freqTable.length) return false;
        emitters[freqIndex] = emitters[freqIndex] == null
                ? new UltrasonicEmitter(freqTable[freqIndex])
                : emitters[freqIndex];

        return emitters[freqIndex].play();
    }

    public boolean kill(int freqIndex){
        if(freqIndex < 0 || freqIndex >= freqTable.length) return false;
        if(emitters[freqIndex] == null) return false;
        if(!emitters[freqIndex].isEchoing) return false;
        return emitters[freqIndex].stop();
    }
}
