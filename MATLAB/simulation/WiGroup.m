%Simulate the Wigroup formation process
%
% 1. Group leader selection
% 2. Other devices connect to group leaders
[matrix,k] = InfoMatrix('random');


[savings, mark] = Performance(matrix,k);
savings
fprintf('# of devices joined= %d',sum(mark));  
%Show the graph
DrawTheGrapgh(matrix,mark,k)


kmatrix = k_center(matrix,k);
[ksavings, kmark] = Performance(kmatrix,k);
ksavings
fprintf('# of devices joined (kcenter)= %d',sum(kmark));  
%Show the graph
figure(2)
DrawTheGrapgh(kmatrix,kmark,k)


% % %Group leader selection
% [matrix,k] = InfoMatrix('normal');
% [savings, mark] = Performance(matrix,k);
% figure(2)
% %Show the graph
% DrawTheGrapgh(matrix,mark,k)



