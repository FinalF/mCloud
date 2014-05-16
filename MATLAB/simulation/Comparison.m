%compare our solution with the k-center and optimal k-center


%1 step: find out the parameter a for optimal solution
%To show that our optimized solution can reach to a upper bound
%define the communication range of each device
cr= 100;

n=50:25:600;
[nr,nc]=size(n);


a=1:0.1:3.5;
[ar,ac]=size(a);

repeat=100;
Total_kopsavings=zeros(nc,ac);
 optimalA=zeros(1,nc);%record the optimal parameter a and savings

for j=1:nc
        for c=1:repeat
            disp('The calculation of: ')
            disp(c);
        [matrix,k] = InfoMatrix('random',n(j));

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

        Total_kopsavings(j,:)=Total_kopsavings(j,:)+koptsavings;
        end
end

  for i=1:nc
      [c,I]=max(Total_kopsavings(i,:));
      optimalA(i)=1+0.1*(I-1); %parameter a
  end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%define the communication range of each device


savings=zeros(1,nc);
ksavings=zeros(1,nc);
koptsavings=zeros(1,nc);


for i=1:nc
    disp('Caculation of saings for # of devices: ')
    disp(n(i))
    for j=1:repeat
        [matrix,k] = InfoMatrix('random',n(i));

        %wigroup solution
        [tmpsavings, mark] = Performance(matrix,k);
        savings(i)=savings(i)+tmpsavings;

        %k-center
        kmatrix = k_center(matrix,k);
        [tmpksavings, kmark] = Performance(kmatrix,k);
        ksavings(i)=ksavings(i)+tmpksavings;

        %optimal k-center   

        koptmatrix = k_center_optimal(matrix,k,optimalA(1,i));
        [tmpkoptsavings, koptmark] = Performance(koptmatrix,k);
        koptsavings(i)=koptsavings(i)+tmpkoptsavings;
    end
end
         savings=savings/repeat;
         ksavings=ksavings/repeat;
         koptsavings=koptsavings/repeat;
      
x=25*(1:nc);   
plot(x,savings,'-*',x,ksavings,'-*',x,koptsavings,'-*');
legend('Wigroup','K-center','optimal','location','best');
title('Uniform Distribution');

fid = fopen('random_distribution.txt','a');
fprintf(fid,'\nWigrout:\n');
% write values at end of file
fprintf(fid,'%f\t', savings);

fprintf(fid,'\nk-center:\n');
% write values at end of file
fprintf(fid,'%f\t', ksavings);

fprintf(fid,'\noptimalk-center:\n');
% write values at end of file
fprintf(fid,'%f\t', koptsavings);

% close the file 
fclose(fid);