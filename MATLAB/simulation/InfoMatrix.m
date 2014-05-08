function [matrix,k] = InfoMatrix(distribution)

%To generate the information of devices
%rows:
%1. de_i: the degree 
%2 x_i: the coordinate
%3. y_i: the coordinate
%4. d_i: the distance to center (d_i is less than R)
%5.a_i: the signal parameter, random in [1,d_i^s]
%6. s_i: signal strength =a_i/d_i^2
%7. cap_i: number of devices can support
%8. common_i: amount of duplicate with others
%----------------------------------------

%The geo area 1000x1000
s=1.8; %signal tuning parameter
R=1000;
n=400;
capacity=5; %maximum number of devices a group leader can support
k=n/capacity;
infoMatrix=zeros(8,n);


if strcmp(distribution,'random')
%step1: generate information
infoMatrix(1,:)=2*pi*rand(1,n);
infoMatrix(4,:)=R/15+14*R/15*rand(1,n);
elseif strcmp(distribution,'normal')
infoMatrix(1,1:n/2)=pi+pi/6*randn(1,n/2);
infoMatrix(1,n/2+1:n)=pi/6+pi/6*randn(1,n/2);
infoMatrix(4,:)=R/2+R/6*randn(1,n);
end
infoMatrix(2,:)=infoMatrix(4,:).*cos(infoMatrix(1,:));
infoMatrix(3,:)=infoMatrix(4,:).*sin(infoMatrix(1,:));
infoMatrix(5,:)=(infoMatrix(4,:).^s-1).*rand(1,n)+1;
% infoMatrix(5,:)=log(infoMatrix(4,:));
infoMatrix(6,:)=infoMatrix(5,:)./infoMatrix(4,:).^2;

infoMatrix(7,:)=floor(capacity+2*randn(1,n));
infoMatrix(8,:)=ones(1,n);


% plot(0,0,'-hr','MarkerSize',8)
% hold on
% for i=1:n
% plot(infoMatrix(2,i),infoMatrix(3,i),'o','MarkerSize',3,'MarkerFaceColor','auto')
% hold on
% end

%step2: sort the matrix, the last k nodes are group leader,mark them out

tmp=infoMatrix;
tmp(1,:) = infoMatrix(6,:); 
tmp(6,:) = infoMatrix(1,:); 
infoMatrix=tmp;

matrix=sortrows(infoMatrix')';

% for i=n-k+1:n
% plot(sortMatrix(2,i),sortMatrix(3,i),'+r','MarkerSize',3)
% hold on
% end

%step3: calculate the stats: how much can we save? Assume that each device
%has an identical unit load.


return