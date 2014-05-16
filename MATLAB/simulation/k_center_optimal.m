function Y = k_center_optimal(X,k,a)
%input: the infomatrix
%a: the expanding parameter
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
%switch chosen column to the end
X(:,[x_index,n-count+1])=X(:,[n-count+1,x_index]);

expandK=floor(a*k);
if expandK >= n
    expandK=n-1;
end

%Deal with outliers: choose a* k group leaders, keep only k with more
%neighbers than the others

%define the communication range of each device
cr= 100;
%%step1: find a*k group leaders
while count<expandK
   %find the next group leader
   maxYC=-inf;
   D=dist(X);
   for i=1:n-count
       tmp=min(D(i,n-count+1:n));
       if tmp>maxYC
           maxYC=tmp;
           x_index=i;
       end
   end
   count=count+1;
   X(:,[x_index,n-count+1])=X(:,[n-count+1,x_index]);    
end

%%step2: only keep k of them
D=distance(X,expandK);  %n-a*k by a*k 
neighbers=zeros(1,n); %record how many neighbers each leader has
% neighbers(2,:)=[n-a*k+1:n];
% Z=logical([neighbers;X]);
Z=[neighbers;X];
for i=1:n-expandK
   for j=1:expandK
       if D(i,j)<cr     
%            disp('debug output: ')
%            disp(n)
%            disp(expandK)
%            disp(j)
           Z(1,n-expandK+j)=Z(1,n-expandK+j)+1;
       end  
   end
end

X=sortrows(Z')';    %the last k columns are group leaders


X(1,:)=[];


Y=X;





    function dist = dist(X)
    %return a n by n matrix which records the distance between each pair of
    %nodes
    [r,n]=size(X);
    dist=inf(n,n);
        for i=1:n-1
            for j=i+1:n
                dist(i,j)=sqrt((X(2,i)-X(2,j))^2+(X(3,i)-X(3,j))^2);
                dist(j,i)=dist(i,j);
            end
        end
    return


return