% A simple implementation of `Backpropagation` in Java.
% MiaoDX MiaoDX@hotmail.com
% Oct, 2016
## What we want to do.

There are lots of amazing open source implements of ML(/DL) libraries,and before diving into these great porjects,I think it really necessary to implement a simple ANN just by hand.In this way,we will not treat these toolkits as `BlackBoxes` and just `Draw` a graph and hope that we will get nice performace after an uncertain time,on the other hand,we make sure that we `CAN` write an ANN or a simple part of it.

In this repo,a java implementation of `Backpropagation` is presented.

## Reference

There are some great resources on the internet that explains BP,some clear,some mathematical,and some just too difficult or too complex for us(me especially) to understand.Here are some articals I think worth reading:

1. [神经网络反向传播的数学原理](https://zhuanlan.zhihu.com/p/22473137) [CHINESE]

2. [A Gentle Introduction to Artificial Neural Networks](https://theclevermachine.wordpress.com/tag/backpropagation/)

3. [Principles of training multi-layer neural network using backpropagation](http://galaxy.agh.edu.pl/%7Evlsi/AI/backp_t_en/backprop.html)

4. [BP（Back Propagation）神经网络及Matlab矩阵实现 ](http://blog.csdn.net/wsywl/article/details/6364744) [CHINESE]

At first,I wrote my codes according to the third article,but later I found that chances are that when calc the error($\delta$),it can be something wrong,and then I found the second one,It's awesome.But I still assist you read the third artical(first),for it's really expressive.

## Design and implementation

To not make this page too complex,THE DESIGN PRINCIPLES AND IMPLEMENTATIONS DETAILS OR TRICKS CAN BE FOUND IN [doc/DesignAndImplement.md](DesignAndImplement.md),IT IS A GOOD ARTICLE -.-.

## ChangeLog

TODO: USE `Guava.Graph` to reimplement it.

20151026: Some visualization work.

20161025: An implement do things ok.

20161004: Basic skeleton.

## Good Luck & Have Fun. -- miaodx