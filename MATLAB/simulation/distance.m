function D = distance(X,k)
%X is a matrix which contains coordinates information
%return a (n-k) by k matrix
[r,n]=size(X);
D=inf(n-k,k);
for i=1:n-k
   for j=n-k+1:n
        D(i,j)=sqrt((X(2,i)-X(2,j))^2+(X(3,i)-X(3,j))^2);
   end
end

D=D(:,n-k+1:n);
return