clear all;
bitNumber = 300;
timeInterval = 1e-6;
frequency = 1e6;

maxHorizontalNoise = 0.6;

max = 40;
x = 1:1:max;
y = 1:1:max;

BER = zeros(max, max);
for a = 1 : 1 : max    
    maxVerticalNoise = 0;
    maxHorizontalNoise = maxHorizontalNoise + a * 0.0025;
    for b = 1 : 1 : max
        errorRate = 0;
        input = randi([0 1], bitNumber, 1);
        modulated = 2 * input - 1;
        signal = rectpulse(modulated, bitNumber);
        t = 0 : (timeInterval/(bitNumber)) : bitNumber * timeInterval / 2 - (timeInterval/(bitNumber));
        t = transpose(t);
        maxVerticalNoise = maxVerticalNoise + a * 0.0025;
        
        %modulating signal
        signalNoised = zeros(bitNumber * bitNumber / 2, 1);
        for i = 1 : 2 : bitNumber
            vn = rand();
            verticalNoise = vn * maxVerticalNoise * 2 - maxVerticalNoise;
            hn = rand();
            horizontalNoise = hn * maxHorizontalNoise * 2 - maxHorizontalNoise;
                if input(i) == 0
                    if input(i + 1) == 0
                        for j = 1 : bitNumber
                            signalNoised((i - 1) / 2 * bitNumber + j) = cos(2 * pi * frequency * t((i - 1) / 2 * bitNumber + j) + horizontalNoise - pi / 4) + verticalNoise;
                        end
                    else 
                        for j = 1 : bitNumber
                            signalNoised((i - 1) / 2 * bitNumber + j) = cos(2 * pi * frequency * t((i - 1) / 2 * bitNumber + j) + horizontalNoise - 7 * pi / 4) + verticalNoise;
                        end
                    end
                else
                    if input(i + 1) == 0
                        for j = 1 : bitNumber
                            signalNoised((i - 1) / 2 * bitNumber + j) = cos(2 * pi * frequency * t((i - 1) / 2 * bitNumber + j) + horizontalNoise - 5 * pi / 4) + verticalNoise;
                        end
                    else 
                        for j = 1 : bitNumber
                            signalNoised((i - 1) / 2 * bitNumber + j) = cos(2 * pi * frequency * t((i - 1) / 2 * bitNumber + j) + horizontalNoise - 3 * pi / 4) + verticalNoise;
                        end
                    end
                end
        end   

        %demodulating signal
        signalDenoised = zeros(bitNumber, 1);
        j = 1;
        for i = 0 : bitNumber : bitNumber * bitNumber / 2 - bitNumber
            if signalNoised(i + 1) < 0 && signalNoised(i + bitNumber / 4) > 0 && signalNoised(i + 3 * bitNumber / 4) < 0
                    signalDenoised(j) = 1;
                    signalDenoised(j + 1) = 1;
            elseif signalNoised(i + 1) > 0 && signalNoised(i + bitNumber / 4) > 0 && signalNoised(i + 3 * bitNumber / 4) < 0
                    signalDenoised(j) = 0;
                    signalDenoised(j + 1) = 0;
            elseif signalNoised(i + 1) < 0 && signalNoised(i + bitNumber / 4) < 0 && signalNoised(i + 3 * bitNumber / 4) > 0
                    signalDenoised(j) = 1;
                    signalDenoised(j + 1) = 0;
            elseif signalNoised(i + 1) > 0 && signalNoised(i + bitNumber / 4) < 0 && signalNoised(i + 3 * bitNumber / 4) > 0
                    signalDenoised(j) = 0;
                    signalDenoised(j + 1) = 1;
            else
                    signalDenoised(j) = round(rand());
                    signalDenoised(j + 1) = round(rand());
            end
                

            j = j + 2;
        end
        
        for i = 1 : 1 : bitNumber
            if signalDenoised(i) ~= input(i)
                errorRate = errorRate + 1;
            end
        end
        BER(a,b) = errorRate/bitNumber;
        
        
        
      
         if a == 1
             if b == 1
                 subplot(3,3,1)
                 plot(t, signalNoised)
                 axis([0 0.00001 -4 4]);
             end
            if b == 10
                 subplot(3,3,2)
                 plot(t, signalNoised)
                 axis([0 0.00001 -4 4]);
             end 
             if b == 20
                 subplot(3,3,3)
                 plot(t, signalNoised)
                 axis([0 0.00001 -4 4]);
             end
         end
         if a == 10
             if b == 1
                 subplot(3,3,4)
                 plot(t, signalNoised)
                 axis([0 0.00001 -4 4]);
             end
             if b == 10
                 subplot(3,3,5)
                 plot(t, signalNoised)
                 axis([0 0.00001 -4 4]);
            end 
             if b == 20
                 subplot(3,3,6)
                 plot(t, signalNoised)
                 axis([0 0.00001 -4 4]);
             end
        end
         if a == 20
             if b == 1
                 subplot(3,3,7)
                 plot(t, signalNoised)
                axis([0 0.00001 -4 4]);
             end
             if b == 10
                 subplot(3,3,8)
                 plot(t, signalNoised)
                 axis([0 0.00001 -4 4]);
             end 
             if b == 20
                 subplot(3,3,9)
                 plot(t, signalNoised)
                 axis([0 0.00001 -4 4]);
             end
         end
        
    end
    disp(a);    
end

figure(4);
surf(x,y,BER)
xlabel('Zak³ócenia "amplitudy"')
ylabel('Zak³ócenia "fazy"')
zlabel('BER')


