close all;
fclose all;
clear all;
clc;
javaclasspath('classes');

filePath = './';
fileName = '12-10-08.EDF';
edfReader = timo.jyu.EDFReader(filePath,fileName);
%edfReader.header.printInfo();

%Prepare structure for data
tempLabels = edfReader.header.getLables();

signalLabels = {};
for i = 1:length(tempLabels)
    signalLabels{i} = tempLabels(i).toCharArray';
end

%Prepare a structure for the data
edfData = struct();
edfData.pointers = [];
edfData.recordNo = [];
edfData.secondsPerRecord = double(edfReader.header.dor);
for i = 1:length(signalLabels)
    edfData.(signalLabels{i}).data = [];
    edfData.(signalLabels{i}).sRate = double(edfReader.header.nSamples.get(i-1))/double(edfReader.header.dor);
end


%Start going through the raw data here
while isempty(edfData.recordNo) == 1 || edfData.recordNo(end) < edfReader.header.ndr
    tempRecord = edfReader.readData(1);
    for r = 1:tempRecord.size()
        if isempty(edfData.recordNo)
            edfData.recordNo = 1;
        else
            edfData.recordNo = [edfData.recordNo; edfData.recordNo(end)+1];
        end
        edfData.pointers = [edfData.pointers; int32(tempRecord.get(r-1).pointer)];
        %Add data from signal channels
        for c = 1:length(signalLabels)
            edfData.(signalLabels{c}).data = [edfData.(signalLabels{c}).data; int16(tempRecord.get(r-1).getSignal(c-1))];
        end
    end
%     keyboard;
    disp(sprintf('Record %d of %d pointer %d',edfData.recordNo(end),edfReader.header.ndr,edfData.pointers(end)));
end
