package com.renygit.myplay;

/**
 * Created by admin on 2017/7/26.
 */

public class SensorGifHelper {

    private double step = 10.0;//取10个维度
    private int totalFrames;//gif总的帧数

    public void setTotalFrames(int totalFrames) {
        this.totalFrames = totalFrames;
        step = (Math.round(step / totalFrames *100)/100.0) * totalFrames;//根据误差让维度调整一点（可能会增大一点点）
    }

    public int getFramesBySensorX(float sensorX){
        //sensorX 介于 stepLeft（正数 约等于5）~ 0 ~ stepRight(负数 约等于-5）之间
        double _sensorX = sensorX + (step / 2);//为计算占比，将可能出现的负数调节为正数（只是边界数） 最右边还是可能为负数 但已不在关注的区域内

        if(_sensorX <= 0){//到最右边边界了 显示最后一帧
            return totalFrames;
        }
        if(_sensorX >= step){//到最左边边界 显示第一帧
            return 1;
        }
        int result = (int)((step - _sensorX) / step * totalFrames);//因为从左到右大约是从10 ~ 0 的情况 如果_sensorX = 8  那么 应该是（10-8）/ 10 = 0.2  返回帧数应该是总帧数*0.2
        if(result == 0){
            result = 1;
        }
        return result;
    }


}
