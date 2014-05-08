function  DrawTheGrapgh(X,Y,k)
%Based on the matrix and Wigropu member mark array
%Plot the scenario in which group leaders and other devices are makred

[r, c] = size(X); 

plot(0,0,'-hr','MarkerSize',8)
hold on
for i=1:c
plot(X(2,i),X(3,i),'o','MarkerSize',4,'MarkerFaceColor','auto')
hold on
end

for i=c-k+1:c
plot(X(2,i),X(3,i),'+r','MarkerSize',4)
hold on
end

%plot participants devices
for i=1:c-k
   if Y(i)==1
       plot(X(2,i),X(3,i),'*g','MarkerSize',4)
        hold on
   end
end

end