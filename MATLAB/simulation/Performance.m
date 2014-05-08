%output the saving
%return a stats matrix S(2 by k)
%1. number of devices joined 
%2. savings
function [savings, mark] = Performance(X,k)
%define the communication range of each device
cr= 100;
%extract the capacity information
[r1,n]=size(X);
capacity=fliplr(X(7,n-k+1:n));  %1 by k capacity from device n to n-k+1
D=fliplr(distance(X,k));  %n-k by k 


mark=zeros(1,n-k); %make a mark on which devices join grops
savings=0;

for i=1:n-k
   %for all other devices, connect to a group leader 1. in the range 2. not
   %congested 
   for j=1:k
       if D(i,j)<cr
           if capacity(j)>0
                savings=savings+X(8,i);
                capacity(j)=capacity(j)-1;
                mark(i)=1;
                break;
           end
       end
       
   end
end

% savings = savings + sum(X(8,n-k+1:n));



function D = distance(X,k)
%X is a matrix which contains coordinates information
%return a (n-k) by k matrix
[r,n]=size(X);
D=inf(n-k,k);
for i=1:n-k
   for j=n-k+1:n
        D(i,j-n+k)=sqrt((X(2,i)-X(2,j))^2+(X(3,i)-X(3,j))^2);
   end
end

% D=D(:,n-k+1:n);
return

return


