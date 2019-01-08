#include <SoftwareSerial.h>
#include <FastGPIO.h>
#define APA102_USE_FAST_GPIO

#include <APA102.h>
const uint8_t dataPin = 3;
const uint8_t clockPin = 2;
APA102<dataPin, clockPin> ledStrip;
const uint16_t ledCount = 16;
const uint16_t minLed =8;
const uint8_t brightness = 1;
rgb_color colors[ledCount];
char bt;
const int cdsInPin = A1;
int cdsvalue=0;
SoftwareSerial I2CBT(10,11); //定義PIN1及PIN0分別為RX及TX腳位 

void setup(){
    Serial.begin(9600); //Arduino起始鮑率9600 
    I2CBT.begin(115200); //藍牙鮑率9600
    //(注意！此鮑率每個藍牙晶片不一定相同，請先確認完再填寫進去) 
    delay(2000);
    Serial.begin(9600);  
}

rgb_color hsvToRgb(uint16_t h, uint8_t s, uint8_t v)
{
    uint8_t f = (h % 60) * 255 / 60;
    uint8_t p = (255 - s) * (uint16_t)v / 255;
    uint8_t q = (255 - f * (uint16_t)s / 255) * (uint16_t)v / 255;
    uint8_t t = (255 - (255 - f) * (uint16_t)s / 255) * (uint16_t)v / 255;
    uint8_t r = 0, g = 0, b = 0;
    switch((h / 60) % 6){
        case 0: r = v; g = t; b = p; break;
        case 1: r = q; g = v; b = p; break;
        case 2: r = p; g = v; b = t; break;
        case 3: r = p; g = q; b = v; break;
        case 4: r = t; g = p; b = v; break;
        case 5: r = v; g = p; b = q; break;
    }
    return rgb_color(r, g, b);
}

void loop()
{
   if (I2CBT.available())
        {  //判斷有沒有訊息接收 
          Serial.print(bt=(I2CBT.read())); 
          Serial.print("\n");  //顯示接收多少訊息 
        }
   uint8_t time = millis() >> 4;
   Serial.print("time = "); Serial.println(time);
  switch (bt) {
      
      case 'A':
          ledStrip.startFrame();
          for(uint16_t i = 0; i < ledCount; i++)
          {
            ledStrip.sendColor(255, 255, 255, 255);  
          }
           ledStrip.endFrame(ledCount);
          break;
      case 'C':
          ledStrip.startFrame();
          for(uint16_t i = 0; i < ledCount; i++)
          {
            ledStrip.sendColor(255, 179, 0, 255);  
          }
           ledStrip.endFrame(ledCount);
          break;
      case 'E':
          //舒眠情境燈
          ledStrip.startFrame();
          for(uint16_t i = 0; i < ledCount; i++)
          {
            ledStrip.sendColor(216, 27, 96, 255);  
          }
           ledStrip.endFrame(ledCount);
          break;
      case 'G':
          //小夜燈
          ledStrip.startFrame();
          for(uint16_t j = 0; j < ledCount; j++)
          {
            ledStrip.sendColor(0, 0, 0, 0);  
          }
          ledStrip.endFrame(ledCount);
           
          ledStrip.startFrame();
          for(uint16_t i = 0; i < minLed; i++)
          {
            ledStrip.sendColor(255, 112, 67, 2);  
          }
           ledStrip.endFrame(ledCount);      
          break;
      case 'K':
              //慶生黃光      
          cdsvalue=analogRead(cdsInPin);
          Serial.println(cdsvalue);
          if (cdsvalue >= 600)         
            {
              //Serial.println(cdsvalue);
              ledStrip.startFrame();
              for(uint16_t i = 0; i < ledCount; i++)
              {
                ledStrip.sendColor(255, 179, 0, 255);  
              }
              ledStrip.endFrame(ledCount); 
            } else {
                 ledStrip.startFrame();
                for(uint16_t i = 0;
                i < ledCount; i++)
                {
                ledStrip.sendColor(0, 0, 0, 0);  
              }
              ledStrip.endFrame(ledCount);
            // LED Turn OFF       
            }        
          delay(2000); 
           break;   
       case '１':
          //白 
          ledStrip.startFrame();
          for(uint16_t j = 0; j < ledCount; j++)
          {
            ledStrip.sendColor(255, 255, 2550, 255);  
          }
          ledStrip.endFrame(ledCount);  
          break;
      case '0':
          //黑 
          ledStrip.startFrame();
          for(uint16_t j = 0; j < ledCount; j++)
          {
            ledStrip.sendColor(0, 0, 0, 0);  
          }
          ledStrip.endFrame(ledCount);  
          break;   
        case 'L':
          //黑 
          ledStrip.startFrame();
          for(uint16_t j = 0; j < ledCount; j++)
          {
            ledStrip.sendColor(0, 0, 0, 0);  
          }
          ledStrip.endFrame(ledCount);  
          break; 
        case 'Z':
          //黑 
          ledStrip.startFrame();
          for(uint16_t j = 0; j < ledCount; j++)
          {
            ledStrip.sendColor(0, 0, 0, 0);  
          }
          ledStrip.endFrame(ledCount);  
          break;            
       case '2':
          //紅 
          ledStrip.startFrame();
          for(uint16_t j = 0; j < ledCount; j++)
          {
            ledStrip.sendColor(255, 0, 0, 255);  
          }
          ledStrip.endFrame(ledCount);  
          break; 
       case '3':
          //橙 
          ledStrip.startFrame();
          for(uint16_t j = 0; j < ledCount; j++)
          {
            ledStrip.sendColor(255, 165, 0, 255);  
          }
          ledStrip.endFrame(ledCount);  
          break; 
       case '4':
          //黃 
          ledStrip.startFrame();
          for(uint16_t j = 0; j < ledCount; j++)
          {
            ledStrip.sendColor(255, 255, 0, 255);  
          }
          ledStrip.endFrame(ledCount);  
          break;                      
      case '5':
          //綠 
          ledStrip.startFrame();
          for(uint16_t j = 0; j < ledCount; j++)
          {
            ledStrip.sendColor(0, 180, 0, 255);  
          }
          ledStrip.endFrame(ledCount);  
          break;
      case '6':
          //藍 
          ledStrip.startFrame();
          for(uint16_t j = 0; j < ledCount; j++)
          {
            ledStrip.sendColor(0, 0, 255, 255);  
          }
          ledStrip.endFrame(ledCount);  
          break;
      case '7':
          //靛
          ledStrip.startFrame();
          for(uint16_t j = 0; j < ledCount; j++)
          { //steel blue
            ledStrip.sendColor(70, 130, 180, 255);  
          }
          ledStrip.endFrame(ledCount);  
          break;
      case '8':
          //紫
          ledStrip.startFrame();
          for(uint16_t j = 0; j < ledCount; j++)
          {
            ledStrip.sendColor(128, 128, 128, 255);  
          }
          ledStrip.endFrame(ledCount);  
          break;                     
      case 'Y':
          // 彩虹模式  
          //Serial.println(time);   
          for(uint16_t i = 0; i < ledCount; i++)
          {
            uint8_t p = time - i * 8;
            colors[i] = hsvToRgb((uint32_t)p * 359 / 256, 255, 255);
            Serial.print("Color = ");Serial.println(colors[i]);
          }
         ledStrip.write(colors, ledCount, brightness);
          delay(10);
          break;
      default:
           ledStrip.startFrame();
          for(uint16_t i = 0; i < ledCount; i++)
          {
            ledStrip.sendColor(0, 0, 0, 0);  
          }
           ledStrip.endFrame(ledCount);
        
          break;   
    }
}
