![Composit](src/main/doclava/custom/assets/composit-template/assets/images/composit-transparent.png?raw=true)

Automatic Web Service Composition Algorithms & Tools for Java.

[![Build Status](https://drone.io/github.com/pablormier/composit/status.png)](https://drone.io/github.com/pablormier/composit/latest)

## Description

ComposIT is an automatic semantic web service composition engine developed within the [Centro de Investigación
en Tecnoloxías da Información (CITIUS)](http://citius.usc.es), University of Santiago de Compostela. 
This library is the main ComposIT Java API used by the engine, which includes different state-of-the-art search algorithms
and optimization techniques to improve the composition of Semantic Web Services.


## License & Citation

This software is licensed under the Apache 2 license, quoted below. You can freely download and use ComposIT in whole or in part, for personal, 
company internal, research or commercial purposes. This license requires you to include a copy of the license in any redistribution 
you may make that includes Apache software. For more information about the terms of this license, please check the
[Apache License FAQ](http://www.apache.org/foundation/license-faq.html#WhatDoesItMEAN).

    Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS),
    University of Santiago de Compostela (USC).

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
### Citation

This library implements an algorithm based on the research work described in http://dx.doi.org/10.4018/jwsr.2012040101.
We encourage you to cite our journal paper if you use ComposIT or some parts of the library in your research:

> Rodriguez-Mier, P., Mucientes, M., Vidal, J. C., & Lama, M. (2012). [An Optimal and Complete Algorithm for 
Automatic Web Service Composition](http://dx.doi.org/10.4018/jwsr.2012040101). *International Journal of Web Services 
Research (IJWSR)*, 9(2), 1-20. doi:10.4018/jwsr.2012040101

```
@article{ijwsr2012rodriguezmier,
  title={An Optimal and Complete Algorithm for Automatic Web Service Composition},
  author={Rodriguez-Mier, Pablo and Mucientes, Manuel and Vidal, Juan C and Lama, Manuel},
  journal={International Journal of Web Services Research (IJWSR)},
  volume={9},
  number={2},
  pages={1--20},
  year={2012},
  publisher={IGI Global}
}
```
#### Paper Abstract

> The ability of web services to build and integrate loosely-coupled systems has attracted a great deal of attention from researchers in the field of the automatic web service composition. The combination of different web services to build complex systems can be carried out using different control structures to coordinate the execution flow and, therefore, finding the optimal combination of web services represents a non-trivial search effort. Furthermore, the time restrictions together with the growing number of available services complicate further the composition problem. In this paper the authors present an optimal and complete algorithm which finds all valid compositions from the point of view of the semantic input-output message structure matching. Given a request, a service dependency graph which represents a suboptimal solution is dynamically generated. Then, the solution is improved using a backward heuristic search based on the A* algorithm which finds all the possible solutions with different number of services and runpath. Moreover, in order to improve the scalability of our approach, a set of dynamic optimization techniques have been included. The proposal has been validated using eight different repositories from the Web Service Challenge 2008, obtaining all optimal solutions with minimal overhead.


