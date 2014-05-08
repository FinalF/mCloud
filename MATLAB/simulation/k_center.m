function Y = k_center[X,k]
%input: the infomatrix
%output: the matrix in which Y(:,n-k+1:n) are group leaders 



%greedy k-center 
%Find k centers such that distance between any point and its 
%closest center is as small as possible
%1. Pick C = {x}, for an arbitrary point x
%2. Repeat until C has k centers:
%Let y maximize d(y, C), where
%d(y, C) = min x in C d(x, y)
%C = C U {y}
[r,n]=size(X);
x_index = ceil((n-1)*rand()+1);
count=1;
while count<k
   %switch chosen column to the end
   X(:,[x_index,n-count+1])=X(:,[n-count+1,x_index]);
   %find the next group leader
   max_index=1;
   for i=1:n-count
       
   end
    
end

function dist = dist(X)
%return a n by n matrix which records the distance between each pair of
%nodes
dist=inf(n,n);
return


return