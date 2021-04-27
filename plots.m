clear;
close all;

%bitsCount = [8 12 16 20 24];
%preimages = [249 4009 79446 1900530 22860714];
%collisions = [2 4 12 46 183];

preimages = load("preimg.txt");
collisions = load("coll.txt");

figure(1);
subplot(2, 1, 1);
plot(preimages(:, 1), preimages(:, 2), '-bo', 'MarkerFaceColor', 'b');
xlabel("Size, bits");
ylabel("Steps, n");
title("Second Preimages");
hold on;
grid on;

subplot(2, 1, 2);
plot(collisions(:, 1), collisions(:, 2), '-go', 'MarkerFaceColor', 'g');
xlabel("Size, bits");
ylabel("Steps, n");
title("Collisions");
hold on;
grid on;