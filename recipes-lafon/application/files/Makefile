#=============SETTINGS=============#
# OS specific part
	RM      = rm -f 
	RMDIR   = rm -rf 
	MKDIR   = mkdir -p
	ERRIGN  = 2>/dev/null
	SEP     = /

#==================================#

# All directories
WORKING_DIR := $(shell pwd)
#SRC_DIR     := src$(SEP)
#INC_DIR     := inc$(SEP)
SRC_DIR     := .$(SEP)
INC_DIR     := .$(SEP)
TEST_DIR    := tests$(SEP)
FONTS_DIR   := font$(SEP)
IMGS_DIR    := img$(SEP)
#LVGL_DIR    := lib$(SEP)
LVGL_DIR    := .$(SEP)
BBUILD_DIR   := build$(SEP)
BIN_DIR     := $(BUILD_DIR)bin$(SEP)
OBJ_DIR     := $(BUILD_DIR)obj$(SEP)

# Extension of the files
#SRC_EXT     := cpp
#HEAD_EXT    := hpp
SRC_EXT     := cpp
HEAD_EXT    := hpp
CSRC_EXT    := c
OBJ_EXT     := o

# Decide whether the commands will be shwon or not
VERBOSE     = FALSE

# Hide or not the calls depending of VERBOSE
ifeq ($(VERBOSE),TRUE)
	HIDE    =
else
	HIDE    = @
endif
#==================================#

#======FONTS======#
CFONTS      := $(wildcard $(FONTS_DIR)*.$(CSRC_EXT))
FONTS_OBJS  := $(patsubst $(FONTS_DIR)%.$(CSRC_EXT), $(OBJ_DIR)font$(SEP)%.$(OBJ_EXT), $(CFONTS))
#=================#

#======IMGS======#
CIMGS       := $(wildcard $(IMGS_DIR)*.$(CSRC_EXT))
IMGS_OBJS   := $(patsubst $(IMGS_DIR)%.$(CSRC_EXT), $(OBJ_DIR)img$(SEP)%.$(OBJ_EXT), $(CIMGS))
#================#

#======LVGL======#
CSRCS       := $(shell find $(LVGL_DIR) -type f -name '*.$(CSRC_EXT)' -not -path '*/\.*')
LV_OBJS     := $(patsubst $(LVGL_DIR)%, $(OBJ_DIR)lib$(SEP)%, $(CSRCS:.$(CSRC_EXT)=.$(OBJ_EXT)))
#================#

#=============SOURCES=============#
SRCS        = $(wildcard $(SRC_DIR)*.$(SRC_EXT))
OBJS        = $(patsubst $(SRC_DIR)%.$(SRC_EXT), $(OBJ_DIR)src$(SEP)%.$(OBJ_EXT), $(SRCS))
#=================================#

#==============TESTS==============#
TSRCS       = $(wildcard $(TEST_DIR)*.$(SRC_EXT))
TOBJS       = $(patsubst $(TEST_DIR)%.$(SRC_EXT), $(OBJ_DIR)tests$(SEP)%, $(TSRCS))
#=================================#


#==============================FLAGS/DEFINES/INCLUDE==============================#
#CFLAGS      := -Wall -O0 -fdata-sections -ffunction-sections --sysroot=$(SYSROOT) -lpthread
CFLAGS      := -O0 -lpthread

DEFINES     := -DDEBUG

INCLUDES    := -I.$(SEP) -Iinc$(SEP) -Ilib$(SEP) -Ilib$(SEP)lv_drivers$(SEP)display$(SEP) -Ilib$(SEP)lv_drivers$(SEP)indev$(SEP) 

LDFLAGS     := --sysroot=$(SYSROOT) -L$(SYSROOT)usr$(SEP)lib$(SEP)arm-linux-gnuabi -L$(SYSROOT)usr$(SEP)lib$(SEP)arm-linux-gnuabi$(SEP)pkgconfig -L$(SYSROOT)lib$(SEP)arm-linux-gnuabi$(SEP)pkgconfig  -L$(SYSROOT)usr$(SEP)lib$(SEP)pkgconfig -Llib -lsqlite3 -lpthread -lm -ldl

CCOMPILE    = $(CC) $(CFLAGS) $(DEFINES) $(INCLUDES)
COMPILE     = $(CXX) $(CFLAGS) -std=gnu++11 $(DEFINES) $(INCLUDES)
#=================================================================================#


#===========EXEC===========#
BIN         := main
#==========================#


#===================================================================================================================================#
#===============================================================RULES===============================================================#
#===================================================================================================================================#

.PHONY: clean build		# Used to indicate they are not file

default: build

all: clean build

test: $(TOBJS)


clean:
	$(RMDIR) $(BUILD_DIR)

build: $(OBJS) $(LV_OBJS) $(FONTS_OBJS) $(IMGS_OBJS)
	$(HIDE)$(MKDIR) $(BIN_DIR)
	$(HIDE)$(CXX) -o $(BIN_DIR)$(BIN) $(OBJS) $(LV_OBJS) $(FONTS_OBJS) $(IMGS_OBJS) -lpthread
	#$(LDFLAGS)


#===================================================================================================================================#
#==========================================================DEBUGGING_RULES==========================================================#
#===================================================================================================================================#
listing :
	$(HIDE)echo "---------------------"
	$(HIDE)echo "SRC:"
	$(HIDE)echo $(SRCS)
	$(HIDE)echo "---------------------"
	$(HIDE)echo "OBJS:"
	$(HIDE)echo $(OBJS)
	$(HIDE)echo "---------------------"

list_symb :
	$(HIDE)echo $(SYMB)


#===================================================================================================================================#
#===============================================================BUILD===============================================================#
#===================================================================================================================================#

$(OBJ_DIR)font$(SEP)%.$(OBJ_EXT): $(FONTS_DIR)%.$(CSRC_EXT)
	$(HIDE)echo 'Building project file: $<'
	$(HIDE)$(MKDIR) $(OBJ_DIR)font$(SEP)
	$(HIDE)$(CCOMPILE) -c $< -o $@

$(OBJ_DIR)img$(SEP)%.$(OBJ_EXT): $(IMGS_DIR)%.$(CSRC_EXT)
	$(HIDE)echo 'Building project file: $<'
	$(HIDE)$(MKDIR) $(OBJ_DIR)img$(SEP)
	$(HIDE)$(CCOMPILE) -c $< -o $@

$(OBJ_DIR)lib$(SEP)%.$(OBJ_EXT): $(LVGL_DIR)%.$(CSRC_EXT)
	$(HIDE)echo 'Building project file: $<'
	$(HIDE)$(MKDIR) $(dir $@)
	$(HIDE)$(CCOMPILE) -c $< -o $@

$(OBJ_DIR)src$(SEP)%.$(OBJ_EXT): $(SRC_DIR)%.$(SRC_EXT) $(INC_DIR)%.${HEAD_EXT}
	$(HIDE)echo 'Building project file: $<'
	$(HIDE)$(MKDIR) $(OBJ_DIR)src$(SEP)
	$(HIDE)$(COMPILE) -MD -MP -c $< -o $@

$(OBJ_DIR)src$(SEP)%.$(OBJ_EXT): $(SRC_DIR)%.$(SRC_EXT)
	$(HIDE)echo 'Building project file: $<'
	$(HIDE)$(MKDIR) $(OBJ_DIR)src$(SEP)
	$(HIDE)$(COMPILE) -MD -MP -c $< -o $@

$(OBJ_DIR)tests$(SEP)%: $(TEST_DIR)%.$(SRC_EXT)
	$(HIDE)echo 'Test project file: $<'
	$(HIDE)$(MKDIR) $(OBJ_DIR)tests$(SEP)
	$(HIDE)$(COMPILE) -c $< -o $@

#======================================================================END_OF_FILE======================================================================#
