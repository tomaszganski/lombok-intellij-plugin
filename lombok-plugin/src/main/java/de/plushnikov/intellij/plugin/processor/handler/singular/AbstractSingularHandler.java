package de.plushnikov.intellij.plugin.processor.handler.singular;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.CommonClassNames;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassType;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiVariable;
import de.plushnikov.intellij.plugin.processor.field.AccessorsInfo;
import de.plushnikov.intellij.plugin.psi.LombokLightFieldBuilder;
import de.plushnikov.intellij.plugin.psi.LombokLightMethodBuilder;
import de.plushnikov.intellij.plugin.util.PsiAnnotationUtil;
import de.plushnikov.intellij.plugin.util.PsiMethodUtil;
import de.plushnikov.intellij.plugin.util.PsiTypeUtil;
import lombok.core.handlers.Singulars;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class AbstractSingularHandler implements BuilderElementHandler {

  @Override
  public void addBuilderField(@NotNull List<PsiField> fields, @NotNull PsiVariable psiVariable, @NotNull PsiClass innerClass, @NotNull AccessorsInfo accessorsInfo) {
    final String fieldName = psiVariable.getName();

    final PsiType fieldType = getBuilderFieldType(psiVariable.getType(), psiVariable.getProject());
    final LombokLightFieldBuilder fieldBuilder =
        new LombokLightFieldBuilder(psiVariable.getManager(), fieldName, fieldType)
            .withModifier(PsiModifier.PRIVATE)
            .withNavigationElement(psiVariable)
            .withContainingClass(innerClass);
    fields.add(fieldBuilder);
  }

  @NotNull
  protected PsiType getBuilderFieldType(@NotNull PsiType psiType, @NotNull Project project) {
    return PsiTypeUtil.getCollectionClassType((PsiClassType) psiType, project, CommonClassNames.JAVA_UTIL_ARRAY_LIST);
  }

  @Override
  public void addBuilderMethod(@NotNull List<PsiMethod> methods, @NotNull PsiVariable psiVariable, @NotNull PsiClass innerClass, boolean fluentBuilder, PsiType returnType, String singularName) {
    final String psiFieldName = psiVariable.getName();
    final PsiType psiFieldType = psiVariable.getType();
    final PsiManager psiManager = psiVariable.getManager();
    final PsiType[] psiParameterTypes = PsiTypeUtil.extractTypeParameters(psiFieldType, psiManager);

    final LombokLightMethodBuilder oneAddMethod = new LombokLightMethodBuilder(psiManager, singularName)
        .withMethodReturnType(returnType)
        .withContainingClass(innerClass)
        .withNavigationElement(psiVariable)
        .withModifier(PsiModifier.PUBLIC)
        .withBody(PsiMethodUtil.createCodeBlockFromText(getOneMethodBody(singularName, psiFieldName, psiParameterTypes, fluentBuilder), innerClass));

    addOneMethodParameter(singularName, psiParameterTypes, oneAddMethod);
    methods.add(oneAddMethod);

    final LombokLightMethodBuilder allAddMethod = new LombokLightMethodBuilder(psiManager, psiFieldName)
        .withMethodReturnType(returnType)
        .withContainingClass(innerClass)
        .withNavigationElement(psiVariable)
        .withModifier(PsiModifier.PUBLIC)
        .withBody(PsiMethodUtil.createCodeBlockFromText(getAllMethodBody(psiFieldName, psiParameterTypes, fluentBuilder), innerClass));

    addAllMethodParameter(psiFieldName, psiFieldType, allAddMethod);
    methods.add(allAddMethod);
  }

  protected abstract void addOneMethodParameter(@NotNull String singularName, @NotNull PsiType[] psiParameterTypes, @NotNull LombokLightMethodBuilder methodBuilder);

  protected abstract void addAllMethodParameter(@NotNull String singularName, @NotNull PsiType psiFieldType, @NotNull LombokLightMethodBuilder methodBuilder);

  protected abstract String getOneMethodBody(@NotNull String singularName, @NotNull String psiFieldName, @NotNull PsiType[] psiParameterTypes, boolean fluentBuilder);

  protected abstract String getAllMethodBody(@NotNull String singularName, @NotNull PsiType[] psiParameterTypes, boolean fluentBuilder);

  public String createSingularName(PsiAnnotation singularAnnotation, String psiFieldName) {
    String singularName = PsiAnnotationUtil.getStringAnnotationValue(singularAnnotation, "value");
    if (StringUtil.isEmptyOrSpaces(singularName)) {
      singularName = Singulars.autoSingularize(psiFieldName);
      if (singularName == null) {
        singularName = psiFieldName;
      }
    }
    return singularName;
  }

  public static boolean validateSingularName(PsiAnnotation singularAnnotation, String psiFieldName) {
    String singularName = PsiAnnotationUtil.getStringAnnotationValue(singularAnnotation, "value");
    if (StringUtil.isEmptyOrSpaces(singularName)) {
      singularName = Singulars.autoSingularize(psiFieldName);
      return singularName != null;
    }
    return true;
  }

  @Override
  public void appendBuildCall(@NotNull StringBuilder buildMethodParameters, @NotNull String fieldName) {
    buildMethodParameters.append(fieldName);
  }
}
