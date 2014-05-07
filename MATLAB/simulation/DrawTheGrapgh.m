function  DrawTheGrapgh(X,Y,k)

[r, c] = size(X); 

plot(0,0,'-hr','MarkerSize',8)
hold on
for i=1:c
plot(X(2,i),X(3,i),'o','MarkerSize',3,'MarkerFaceColor','auto')
hold on
end

for i=c-k+1:c
plot(X(2,i),X(3,i),'+r','MarkerSize',3)
hold on
end

%plot participants devices
for i=1:c-k
   if Y(i)==1
       plot(X(2,i),X(3,i),'*g','MarkerSize',3)
        hold on
   end
end

end