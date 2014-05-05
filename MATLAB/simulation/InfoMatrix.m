%To generate the information of devices
%1. de_i: the degree 
%2 x_i: the coordinate
%3. y_i: the coordinate
%4. d_i: the distance to center
%5.a_i: the signal parameter, random in [1,d_i]
%6. s_i: signal strength =a_i/d_i^2
%----------------------------------------

%The geo area 1000x1000
R=1000;
n=200;
infoMatrix=zeros(6,n);



%step1: generate coordinates
infoMatrix(1,:)=2*pi*rand(1,n);
infoMatrix(4,:)=R*rand(1,n);
infoMatrix(2,:)=infoMatrix(4,:).*cos(infoMatrix(1,:));
infoMatrix(3,:)=infoMatrix(4,:).*sin(infoMatrix(1,:));

plot(0,0,'-hr','MarkerSize',12)
hold on
for i=1:n
plot(infoMatrix(2,i),infoMatrix(3,i),'o','MarkerSize',3,'MarkerFaceColor','auto')
hold on
end

pdecirc(0,0,R)