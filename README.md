![Composit](src/main/doclava/custom/assets/composit-template/assets/images/composit-transparent.png?raw=true)

Automatic Web Service Composition Algorithms & Tools for Java.

[![Build Status](https://travis-ci.org/citiususc/composit.svg?branch=master)](http://goo.gl/rZxkXt)

## Description

ComposIT is an automatic semantic web service composition engine developed within the [Centro de Investigación
en Tecnoloxías da Información (CITIUS)](http://goo.gl/DBtaht), University of Santiago de Compostela. 
This library is the main ComposIT Java API used by the engine, which includes different state-of-the-art search algorithms
and optimization techniques to improve the composition of Semantic Web Services.

## Documentation

ComposIT JavaDoc: [0.1.0-SNAPSHOT](http://goo.gl/XGev1i)

## FAQ

- (Q1) _I've tried to compile the project with maven on windows but the "maven-exec-plugin" throws the following error_: `Failed to execute goal org.codehaus.mojo:exec-maven-plugin:1.2.1:exec (install-hipster) on project composit-parent: Command execution failed. Cannot run program "mvn"`:
- (A1) The simplest way to fix the problem is by manually installing the missing dependency (`lib/hipster-core-0.0.1-SNAPSHOT`) with maven:

    `mvn install:install-file -Dfile=path-to-composit/lib/hipster-core-0.0.1-SNAPSHOT.jar -DgroupId=es.usc.citius.lab -DartifactId=hipster-core -Dversion=0.0.1-SNAPSHOT -Dpackaging=jar`




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
Automatic Web Service Composition](http://goo.gl/rH59tu). *International Journal of Web Services 
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

### Acknowledgements

* [Ángel Piñeiro](https://github.com/angelpinheiro) for the design of the logo.
