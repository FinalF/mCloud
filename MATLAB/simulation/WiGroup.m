%Simulate the Wigroup formation process
%
% 1. Group leader selection
% 2. Other devices connect to group leaders

%define the communication range of each device
cr= 100;
n=100;

[matrix,k] = InfoMatrix('random',n);


[savings, mark] = Performance(matrix,k);
savings
fprintf('# of devices joined= %d',sum(mark));  
%Show the graph
DrawTheGrapgh(matrix,mark,k)
title('Wigroup','fontsize',18);
set(gca,'fontsize',15);

kmatrix = k_center(matrix,k);
[ksavings, kmark] = Performance(kmatrix,k);
ksavings
fprintf('# of devices joined (kcenter)= %d',sum(kmark));  
%Show the graph
figure(2)
DrawTheGrapgh(kmatrix,kmark,k)
title('K-center','fontsize',18);
set(gca,'fontsize',15);

koptmatrix = k_center_optimal(matrix,k,2);
[koptsavings, koptmark] = Performance(koptmatrix,k);
koptsavings
fprintf('# of devices joined (koptcenter)= %d',sum(koptmark));  
%Show the graph
figure(3)
DrawTheGrapgh(koptmatrix,koptmark,k)
title('Optimal','fontsize',18);
set(gca,'fontsize',15);



