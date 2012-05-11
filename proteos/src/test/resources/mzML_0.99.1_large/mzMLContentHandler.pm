package mzMLContentHandler;
###############################################################################
###############################################################################
###############################################################################
# mzMLContentHandler package: SAX parser callback routines
#
# This mzMLContentHandler package defines all the content handling callback
# subroutines used the SAX parser
###############################################################################
use strict;
use XML::Xerces;
use Date::Manip;
use vars qw(@ISA $VERBOSE);
@ISA = qw(XML::Xerces::PerlContentHandler);
$VERBOSE = 0;


###############################################################################
# new
###############################################################################
sub new {
  my $class = shift;
  my $self = $class->SUPER::new();
  $self->object_stack([]);
  $self->unhandled({});
  return $self;
}


###############################################################################
# object_stack
###############################################################################
sub object_stack {
  my $self = shift;
  if (scalar @_) {
    $self->{OBJ_STACK} = shift;
  }
  return $self->{OBJ_STACK};
}


###############################################################################
# setVerbosity
###############################################################################
sub setVerbosity {
  my $self = shift;
  if (scalar @_) {
    $VERBOSE = shift;
  }
}


###############################################################################
# unhandled
###############################################################################
sub unhandled {
  my $self = shift;
  if (scalar @_) {
    $self->{UNHANDLED} = shift;
  }
  return $self->{UNHANDLED};
}


###############################################################################
# start_element
###############################################################################
sub start_element {
  my ($self,$uri,$localname,$qname,$attrs) = @_;

  #### Make a hash to of the attributes
  my %attrs = $attrs->to_hash();

  #### Convert all the values from hashref to single value
  while (my ($aa1,$aa2) = each (%attrs)) {
    $attrs{$aa1} = $attrs{$aa1}->{value};
  }


  #### Check that the cv exists
  unless ($self->{cv}) {
    $self->readControlledVocabularyFile();
  }

  ##########################################################################
  #### If this is a spectrum, then store some attributes
  if ($localname eq 'spectrum') {
    $self->{spectrum_counter}++;
  }

  #### If this is a cvParam, check that name matches the accession
  if ($localname eq 'cvParam') {
    my $accession = $attrs{accession};
    my $name = $attrs{name};
    if ($self->{cv}->{status} eq 'read ok') {
      if ($self->{cv}->{terms}->{$accession}) {
	if ($self->{cv}->{terms}->{$accession}->{name} eq $name) {
	  #print "INFO: $accession = $name matches CV\n";
	  $self->{cv}->{n_valid_terms}++;
	} else {
	  #print "WARNING: $accession should be ".
	  #  "'$self->{cv}->{terms}->{$accession}->{name}' instead of '$name'\n";
	  $self->{cv}->{mislabeled_terms}++;
	  print "replaceall.pl \"$name\" \"$self->{cv}->{terms}->{$accession}->{name}\" \$file\n";
	}
      } else {
	print "WARNING: CV term $accession ('$name') is not in the cv\n";
	$self->{cv}->{unrecognized_terms}++;
      }
    } else {
      $self->{cv}->{n_uncheckable_terms}++;
    }
  }

  #### Increase the counters and print some progress info
  $self->{counter}++;
  print $self->{counter}."..." if ($VERBOSE && $self->{counter} % 100 == 0);


  #### Push information about this element onto the stack
  my $tmp;
  $tmp->{name} = $localname;
  push(@{$self->object_stack},$tmp);


} # end start_element



###############################################################################
# end_element
###############################################################################
sub end_element {
  my ($self,$uri,$localname,$qname) = @_;


  #### If there's an object on the stack consider popping it off
  if (scalar @{$self->object_stack()}){

    #### If the top object on the stack is the correct one, pop it off
    #### else die bitterly
    if ($self->object_stack->[-1]->{name} eq "$localname") {
      pop(@{$self->object_stack});
    } else {
      die("STACK ERROR: Wanted to pop off an element fo type '$localname'".
        " but instead we found '".$self->object_stack->[-1]->{name}."'!");
    }

  } else {
    die("STACK ERROR: Wanted to pop off an element of type '$localname'".
        " but instead we found the stack empty!");
  }

}

###############################################################################
# readControlledVocabularyFile
###############################################################################
sub readControlledVocabularyFile {
  my $METHOD = 'readControlledVocabularyFile';
  my $self = shift || die ("self not passed");
  my %args = @_;

  my $input_file = $args{input_file};

  $self->{cv}->{status} = 'initialized';

  unless ($input_file) {
    $input_file = 'psi-ms.obo';
  }

  #### Check to see if file exists
  unless (-e $input_file) {
    print "ERROR: controlled vocabulary file '$input_file' does not exist\n";
    print "WARNING: NOT CHECKING CV TERMS!!!\n";
    return;
  }

  #### Open file
  unless (open(INFILE,$input_file)) {
    print "ERROR: Unable to open controlled vocabulary file '$input_file'\n";
    print "WARNING: NOT CHECKING CV TERMS!!!\n";
    return;
  }
  print "INFO: Reading cv file '$input_file'\n";


  #### Read in file
  #### Very simple reader with no sanity/error checking
  my ($line,$id,$name);
  while ($line = <INFILE>) {
    $line =~ s/[\r\n]//g;
    if ($line =~ /^id: (\S+)\s*/) {
      $id = $1;
    }
    if ($line =~ /^name: (.+)\s*$/) {
      $name = $1;
      $self->{cv}->{terms}->{$id}->{name} = $name;
    }
  }


  close(INFILE);
  $self->{cv}->{status} = 'read ok';

  return;

}

###############################################################################

1;
