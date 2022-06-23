graph [
	multigraph 1
    node [
    id 2
    label " CL_2"
    weight " 2"
    coalition " true"
  ]
    node [
    id 3
    label " CL_3"
    weight " 3"
    coalition " true"
  ]
    node [
    id 1
    label " CL_1"
    weight " 5"
    coalition " true"
  ]
    node [
    id 0
    label " CL_0"
    weight " 6"
    coalition " true"
  ]
  edge [
    source 2
    target 1
    sign NEGATIVE
  ]
  edge [
    source 2
    target 0
    sign NEGATIVE
  ]
  edge [
    source 3
    target 0
    sign NEGATIVE
  ]
  edge [
    source 1
    target 2
    sign NEGATIVE
  ]
  edge [
    source 1
    target 0
    sign NEGATIVE
  ]
  edge [
    source 0
    target 2
    sign NEGATIVE
  ]
  edge [
    source 0
    target 3
    sign NEGATIVE
  ]
  edge [
    source 0
    target 1
    sign NEGATIVE
  ]
]