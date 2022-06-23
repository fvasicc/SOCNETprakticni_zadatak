graph [
	multigraph 1
    node [
    id 2
    label " CL_2"
    weight " 2"
  ]
    node [
    id 1
    label " CL_1"
    weight " 12"
  ]
    node [
    id 0
    label " CL_0"
    weight " 4"
  ]
  edge [
    source 1
    target 0
    sign NEGATIVE
  ]
  edge [
    source 0
    target 1
    sign NEGATIVE
  ]
]