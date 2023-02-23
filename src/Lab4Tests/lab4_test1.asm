# lab4_test1.asm
#
# CPI = 1.400	cycles = 42	instructions = 30

# instructions with rd as destination

lw $a0, 0($a1)
add $t0, $a0, $a1	# stall 3

lw $a0, 0($a1)
add $t0, $a1, $a0	# stall 6

lw $a0, 0($a1)
add $t0, $t0, $t0	# no stall 8

lw $a0, 0($a1)
sub $t0, $a1, $a0	# stall 11

lw $a0, 0($a1)
sub $t0, $a0, $a1	# stall 14

lw $a0, 0($a1)
sub $t0, $t0, $t0	# no stall 16

lw $a0, 0($a1)
slt $t0, $a0, $a1	# stall 19

lw $a0, 0($a1)
slt $t0, $a1, $a0	# stall 22

lw $a0, 0($a1)
slt $t0, $t0, $t0	# no stall 24

# instructions with rt as destination

lw $a0, 0($a1)		# no stall 26
addi $t0, $a1, 1

lw $a0, 0($a1)		# stall 29
addi $t0, $a0, 1

lw $a0, 0($a1)		# no stall 31
addi $a0, $t0, 1

lw $a0, 0($a1)
lw $a0, 0($a1)		# no stall 33

lw $a0, 0($a1)
lw $a1, 0($a0)		# stall 36

lw $0, 0($s1)		# no stall 38
add $s2, $s1, $0