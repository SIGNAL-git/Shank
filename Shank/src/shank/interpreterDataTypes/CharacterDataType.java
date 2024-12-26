package shank.interpreterDataTypes;

import shank.parserNodes.CharacterNode;

public class CharacterDataType extends InterpreterDataType {
	
	private char characterData;
	
	public CharacterDataType(char characterData)
	{
		this.characterData = characterData;
	}
	
	public CharacterDataType(CharacterNode characterData)
	{
		this.characterData = characterData.getValue();
	}
	
	public char getValue()
	{
		return characterData;
	}
	
	public void setValue(char characterData)
	{
		this.characterData = characterData;
	}
	
	public String toString()
	{
		return String.valueOf(characterData);
	}
	
	public void fromString(String characterData)
	{
		this.characterData = characterData.toCharArray()[0];
	}
	
}