# compute fibonacci numbers
#
# 10th fibonacci number = 55
# CPI = 1.414	cycles = 1694	instructions = 1198

addi $a0, $0, 10	# input argument  0
addi $sp, $0, 4095	# initialize stack pointer  1
jal fibonacci  # 2
j end   # 3

fibonacci:	addi $t0, $0, 3   # 4
		slt $t1, $a0, $t0    # 5
		bne $0, $t1, basecase	# 6 check if argument is less than 3

		addi $sp, $sp, -3  # 7
		sw $a0, 0($sp)   # 8
		sw $ra, 1($sp)    # 9
		addi $a0, $a0, -1   # 10
		jal fibonacci		#   11 compute fibonacci(n-1)

		sw $v0, 2($sp)   # 12
		lw $a0, 0($sp)    #  13
		addi $a0, $a0, -2   # 14
		jal fibonacci		# 15  compute fibonacci(n-2)
		lw $t0, 2($sp)    # 16
		add $v0, $v0, $t0    # 17

		lw $ra, 1($sp)   # 18
		addi $sp, $sp, 3    # 19
		jr $ra     # 20

basecase:	addi $v0, $0, 1  # 21
		jr $ra   #22


end: add $0, $0, $0    # 23