%To generate the information of devices
%1. de_i: the degree 
%2 x_i: the coordinate
%3. y_i: the coordinate
%4. d_i: the distance to center (d_i is less than R)
%5.a_i: the signal parameter, random in [1,d_i]
%6. s_i: signal strength =a_i/d_i^2
%----------------------------------------

%The geo area 1000x1000
R=1000;
n=200;
capacity=10; %maximum number of devices a group leader can support
k=n/capacity;
infoMatrix=zeros(6,n);



%step1: generate information
infoMatrix(1,:)=2*pi*rand(1,n);
infoMatrix(4,:)=R/15+14*R/15*rand(1,n);
infoMatrix(2,:)=infoMatrix(4,:).*cos(infoMatrix(1,:));
infoMatrix(3,:)=infoMatrix(4,:).*sin(infoMatrix(1,:));
infoMatrix(5,:)=(infoMatrix(4,:).^2-1).*rand(1,n)+1;
infoMatrix(6,:)=infoMatrix(5,:)./infoMatrix(4,:).^2;




plot(0,0,'-hr','MarkerSize',8)
hold on
for i=1:n
plot(infoMatrix(2,i),infoMatrix(3,i),'o','MarkerSize',3,'MarkerFaceColor','auto')
hold on
end

%step2: sort the matrix, the last k nodes are group leader
[r, c] = size(infoMatrix); 
tmp=infoMatrix;
tmp(1,:) = infoMatrix(r,:); 
tmp(r,:) = infoMatrix(1,:); 
infoMatrix=tmp;

sortMatrix=sortrows(infoMatrix')';

for i=n-k+1:n
plot(sortMatrix(2,i),sortMatrix(3,i),'+r','MarkerSize',3)
hold on
end