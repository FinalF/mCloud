%To show that our optimized solution can reach to a upper bound
%define the communication range of each device
cr= 100;



a=1:0.1:3.5;
[ar,ac]=size(a);

repeat=5;
Total_kopsavings=zeros(1,ac);
for c=1:repeat
    disp('The calculation of: ')
    disp(c);
[matrix,k] = InfoMatrix('random',100);

%R=1000;
% n=400;
% capacity=5; %maximum number of devices a group leader can support
% k=n/capacity;

koptsavings=zeros(1,ac);

for i=1:ac
koptmatrix = k_center_optimal(matrix,k,a(i));
[koptsavings(i), koptmark] = Performance(koptmatrix,k);
% fprintf('# of devices joined (koptcenter)= %d',sum(koptmark));  
end

Total_kopsavings=Total_kopsavings+koptsavings;

end
plot(a,Total_kopsavings/repeat,'-*','LineWidth',2);
legend boxoff
xlabel('a','fontsize',18);
ylabel('Bandwidth Saving','fontsize',18);

